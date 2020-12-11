package edu.pedorenko.petrinode.service;

import edu.pedorenko.petri.dto.ArcDTO;
import edu.pedorenko.petri.dto.ArcInDTO;
import edu.pedorenko.petri.dto.DelayGeneratorDTO;
import edu.pedorenko.petri.dto.DelayGeneratorTypeDTO;
import edu.pedorenko.petri.dto.ExternalArcDTO;
import edu.pedorenko.petri.dto.PlaceDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectDTO;
import edu.pedorenko.petri.dto.TransitionDTO;
import edu.pedorenko.petrinode.exception.PetriObjectModelCreatingException;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObjectModel;
import edu.pedorenko.petrinode.model.event_protocol.DummyEventProtocolFactory;
import edu.pedorenko.petrinode.model.model.PetriObject;
import edu.pedorenko.petrinode.model.model.PetriObjectModel;
import edu.pedorenko.petrinode.model.model.PetriObjectModelException;
import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.NormalDelayGenerator;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.UniformDelayGenerator;
import edu.pedorenko.petrinode.model.model_computer.PetriObjectModelComputer;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetriObjectCreatingService {

    public ParallelComputingPetriObjectModel createPetriObjectModel(PreprocessedPetriObjectDTO petriObjectDTO) {

        List<Transition> transitions = createTransitions(petriObjectDTO.getTransitions(), petriObjectDTO.getPlaces());

        List<PetriObject> petriObjects = new ArrayList<>();

        PetriObject requiredPetriObject;
        try {
            requiredPetriObject = new PetriObject(petriObjectDTO.getPetriObjectIds(), petriObjectDTO.getPetriObjectName(), transitions, 1, Integer.MAX_VALUE);
            petriObjects.add(requiredPetriObject);
        } catch (PetriObjectModelException ex) {
            throw new PetriObjectModelCreatingException(ex.getMessage());
        }

        int transitionIdCounter = -1;
        int placeIdCounter = -1;

        try {
            for (ExternalArcDTO externalArc : petriObjectDTO.getExternalArcs()) {
                if (externalArc.getFromPetriObjectId() == petriObjectDTO.getPetriObjectIds().get(0)) {
                    PetriObject nextPetriObject = createNextPetriObject(externalArc.getToPetriObjectId(), externalArc.getToPlaceId(), transitionIdCounter, placeIdCounter);
                    transitionIdCounter--;
                    placeIdCounter--;
                    petriObjects.add(nextPetriObject);

                    requiredPetriObject.getTransitionById(externalArc.getFromTransitionId()).addArcTo(nextPetriObject.getPlaceById(externalArc.getToPlaceId()));
                } else {
                    PetriObject prevPetriObject = createPrevPetriObject(externalArc.getFromPetriObjectId(), externalArc.getFromTransitionId(), placeIdCounter);
                    petriObjects.add(prevPetriObject);

                    prevPetriObject.getTransitionById(externalArc.getFromTransitionId()).addArcTo(requiredPetriObject.getPlaceById(externalArc.getToPlaceId()));
                }
            }

            PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjects);
            return PetriObjectModelComputer.createParallelComputingPetriObjectModel(petriObjectDTO.getPetriObjectIds().get(0), petriObjectModel, new DummyEventProtocolFactory());

        } catch (PetriObjectModelException ex) {
            throw new PetriObjectModelCreatingException(ex.getMessage());
        }
    }

    private PetriObject createNextPetriObject(long toPetriObjectId, long toPlaceId, int transitionId, int placeId) throws PetriObjectModelException {

        Transition transition = new Transition(transitionId, "");
        transition.addArcFrom(new Place(toPlaceId, ""));
        transition.addArcTo(new Place(placeId, ""));

        return new PetriObject(toPetriObjectId, "", new ArrayList<Transition>() {{
            add(transition);
        }});
    }

    private PetriObject createPrevPetriObject(long fromPetriObjectId, long fromTransitionId, int placeId) throws PetriObjectModelException {

        Transition transition = new Transition(fromTransitionId, "");
        transition.addArcFrom(new Place(placeId, ""));


        return new PetriObject(fromPetriObjectId, "", new ArrayList<Transition>() {{
            add(transition);
        }});
    }

    private List<Transition> createTransitions(List<TransitionDTO> transitionDTOs, List<PlaceDTO> placeDTOs) {

        List<Transition> transitions = new ArrayList<>();

        Map<Long, Place> places = createPlaces(placeDTOs);

        for (TransitionDTO transitionDTO : transitionDTOs) {

            Transition transition = createTransition(transitionDTO, places);

            transitions.add(transition);
        }

        return transitions;
    }

    private Map<Long, Place> createPlaces(List<PlaceDTO> placeDTOs) {

        Map<Long, Place> places = new HashMap<>();

        for (PlaceDTO placeDTO : placeDTOs) {

            Place place = createPlace(placeDTO);

            if (places.containsKey(place.getPlaceId())) {
                throw new PetriObjectModelCreatingException("Duplicate place id \"" + place.getPlaceId() + "\"");
            }

            places.put(place.getPlaceId(), place);
        }

        return places;
    }

    private Place createPlace(PlaceDTO placeDTO) {

        return new Place(
                placeDTO.getPlaceId(),
                placeDTO.getPlaceName(),
                placeDTO.getMarking(),
                placeDTO.isCollectStatistics());
    }

    private Transition createTransition(TransitionDTO transitionDTO, Map<Long, Place> places) {

        DelayGenerator delayGenerator = createDelayGenerator(transitionDTO.getDelayGenerator());

        Transition transition = new Transition(
                transitionDTO.getTransitionId(),
                transitionDTO.getTransitionName(),
                transitionDTO.getPriority(),
                transitionDTO.getProbability(),
                delayGenerator,
                transitionDTO.isCollectStatistics());

        for (ArcInDTO arcDTO : transitionDTO.getArcsIn()) {
            Place fromPlace = places.get(arcDTO.getPlaceId());

            if (fromPlace == null) {
                throw new PetriObjectModelCreatingException(
                        "No place with id \"" + arcDTO.getPlaceId() + "\". " +
                                "Check arcs for transition with id \"" + transitionDTO.getTransitionId() + "\"");
            }

            if (arcDTO.isInformational()) {
                transition.addInformationalArcFrom(fromPlace, arcDTO.getMultiplicity());
            } else {
                transition.addArcFrom(fromPlace, arcDTO.getMultiplicity());
            }
        }

        for (ArcDTO arcDTO : transitionDTO.getArcsOut()) {
            Place toPlace = places.get(arcDTO.getPlaceId());

            if (toPlace == null) {
                throw new PetriObjectModelCreatingException(
                        "No place with id \"" + arcDTO.getPlaceId() + "\". " +
                                "Check arcs for transition with id \"" + transitionDTO.getTransitionId() + "\"");
            }

            transition.addArcTo(toPlace, arcDTO.getMultiplicity());
        }

        return transition;
    }

    private DelayGenerator createDelayGenerator(DelayGeneratorDTO delayGeneratorDTO) {

        DelayGeneratorTypeDTO delayGeneratorType = delayGeneratorDTO.getType();

        switch (delayGeneratorType) {

            case CONSTANT:
                return new ConstantDelayGenerator(delayGeneratorDTO.getFirstParam());

            case UNIFORM:
                return new UniformDelayGenerator(delayGeneratorDTO.getFirstParam(), delayGeneratorDTO.getSecondParam());

            case NORMAL:
                return new NormalDelayGenerator(delayGeneratorDTO.getFirstParam(), delayGeneratorDTO.getSecondParam());

            case EXPONENTIAL:
                return new ExponentialDelayGenerator(delayGeneratorDTO.getFirstParam());

            default:
                throw new PetriObjectModelCreatingException("Unrecognized delay generator type: " + delayGeneratorType.name());
        }
    }
}
