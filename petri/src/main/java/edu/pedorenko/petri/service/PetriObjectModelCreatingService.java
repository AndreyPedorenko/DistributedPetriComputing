package edu.pedorenko.petri.service;

import edu.pedorenko.petri.dto.ArcDTO;
import edu.pedorenko.petri.dto.ArcInDTO;
import edu.pedorenko.petri.dto.DelayGeneratorDTO;
import edu.pedorenko.petri.dto.DelayGeneratorTypeDTO;
import edu.pedorenko.petri.dto.ExternalArcDTO;
import edu.pedorenko.petri.dto.PetriObjectDTO;
import edu.pedorenko.petri.dto.PetriObjectModelDTO;
import edu.pedorenko.petri.dto.PlaceDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectModelDTO;
import edu.pedorenko.petri.dto.TransitionDTO;
import edu.pedorenko.petri.exception.PetriObjectModelCreatingException;
import edu.pedorenko.petri.model.PetriObject;
import edu.pedorenko.petri.model.PetriObjectModel;
import edu.pedorenko.petri.model.PetriObjectModelException;
import edu.pedorenko.petri.model.arc.ArcIn;
import edu.pedorenko.petri.model.arc.ArcOut;
import edu.pedorenko.petri.model.arc.InformationalArcIn;
import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;
import edu.pedorenko.petri.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petri.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petri.model.transition.delay_generator.NormalDelayGenerator;
import edu.pedorenko.petri.model.transition.delay_generator.UniformDelayGenerator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetriObjectModelCreatingService {

    public PetriObjectModel createPetriObjectModel(PetriObjectModelDTO petriObjectModelDTO) {

        Map<Long, PetriObject> petriObjects = createPetriObjects(petriObjectModelDTO.getPetriObjects());

        for (ExternalArcDTO externalArcDTO : petriObjectModelDTO.getExternalArcs()) {

            PetriObject fromPetriObject = petriObjects.get(externalArcDTO.getFromPetriObjectId());
            PetriObject toPetriObject = petriObjects.get(externalArcDTO.getToPetriObjectId());

            if (fromPetriObject == null) {
                throw new PetriObjectModelCreatingException(
                        "No Petri object with id \"" + externalArcDTO.getFromPetriObjectId() + "\". Check external arcs");
            }

            if (toPetriObject == null) {
                throw new PetriObjectModelCreatingException(
                        "No Petri object with id \"" + externalArcDTO.getToPetriObjectId() + "\". Check external arcs");
            }

            Transition fromTransition = fromPetriObject.getTransitionById(externalArcDTO.getFromTransitionId());

            if (fromTransition == null) {
                throw new PetriObjectModelCreatingException(
                        "No transition with id \"" + externalArcDTO.getFromTransitionId() + "\"" +
                                " in Petri object with id \"" + externalArcDTO.getFromPetriObjectId() + "\"." +
                                " Check external arcs");
            }

            Place toPlace = toPetriObject.getPlaceById(externalArcDTO.getToPlaceId());

            if (toPlace == null) {
                throw new PetriObjectModelCreatingException(
                        "No place with id \"" + externalArcDTO.getToPlaceId() + "\"" +
                                " in Petri object with id \"" + externalArcDTO.getToPetriObjectId() + "\"." +
                                " Check external arcs");
            }

            fromTransition.addArcTo(toPlace, externalArcDTO.getMultiplicity());
        }

        try {
            return new PetriObjectModel(new ArrayList<>(petriObjects.values()));
        } catch (PetriObjectModelException ex) {
            throw new PetriObjectModelCreatingException(ex.getMessage());
        }
    }

    private Map<Long, PetriObject> createPetriObjects(List<PetriObjectDTO> petriObjectDTOs) {

        Map<Long, PetriObject> petriObjects = new HashMap<>();

        for (PetriObjectDTO petriObjectDTO : petriObjectDTOs) {

            PetriObject petriObject = createPetriObject(petriObjectDTO);

            petriObjects.put(petriObject.getPetriObjectId(), petriObject);
        }

        return petriObjects;
    }

    private PetriObject createPetriObject(PetriObjectDTO petriObjectDTO) {

        List<Transition> transitions = createTransitions(petriObjectDTO.getTransitions(), petriObjectDTO.getPlaces());

        try {
            return new PetriObject(petriObjectDTO.getPetriObjectId(), petriObjectDTO.getPetriObjectName(), transitions);
        } catch (PetriObjectModelException ex) {
            throw new PetriObjectModelCreatingException(ex.getMessage());
        }
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

    public PreprocessedPetriObjectModelDTO createPreprocessedPetriObjectModelDTO(
            PetriObjectModel preprocessedPetriObjectModel,
            double timeModelling) {

        Map<Long, List<ExternalArcDTO>> externalArcsDTOsMap = createExternalArcsDTOsMap(preprocessedPetriObjectModel);

        List<PreprocessedPetriObjectDTO> preprocessedPetriObjectDTOs =
                createPreprocessedPetriObjectDTOs(preprocessedPetriObjectModel, externalArcsDTOsMap, timeModelling);

        return new PreprocessedPetriObjectModelDTO(preprocessedPetriObjectDTOs);
    }

    private List<PreprocessedPetriObjectDTO> createPreprocessedPetriObjectDTOs(
            PetriObjectModel petriObjectModel,
            Map<Long, List<ExternalArcDTO>> externalArcsDTOsMap,
            double timeModelling) {

        List<PreprocessedPetriObjectDTO> preprocessedPetriObjectDTOs = new ArrayList<>();

        for (PetriObject petriObject : petriObjectModel) {

            List<ExternalArcDTO> externalArcDTOs = externalArcsDTOsMap.get(petriObject.getPetriObjectId());
            if (externalArcDTOs == null) {
                externalArcDTOs = new ArrayList<>();
            }

            PreprocessedPetriObjectDTO preprocessedPetriObjectDTO = createPreprocessedPetriObjectDTO(
                    petriObject,
                    externalArcDTOs,
                    timeModelling);

            preprocessedPetriObjectDTOs.add(preprocessedPetriObjectDTO);
        }

        return preprocessedPetriObjectDTOs;
    }

    private PreprocessedPetriObjectDTO createPreprocessedPetriObjectDTO(
            PetriObject petriObject,
            List<ExternalArcDTO> externalArcDTOs,
            double timeModelling) {

        List<Long> petriObjectIds = petriObject.getPetriObjectIds();
        String petriObjectName = petriObject.getPetriObjectName();
        List<PlaceDTO> placeDTOs = createPlaceDTOs(petriObject);
        List<TransitionDTO> transitionDTOs = createInnerTransitionDTOs(petriObject);

        return new PreprocessedPetriObjectDTO(
                petriObjectIds,
                petriObjectName,
                placeDTOs,
                transitionDTOs,
                externalArcDTOs,
                timeModelling);
    }

    private List<PlaceDTO> createPlaceDTOs(PetriObject petriObject) {

        List<PlaceDTO> placeDTOs = new ArrayList<>();

        for (Place place : petriObject.getThisObjectPlaces()) {

            PlaceDTO placeDTO = createPlaceDTO(place);

            placeDTOs.add(placeDTO);
        }

        return placeDTOs;
    }

    private PlaceDTO createPlaceDTO(Place place) {

        return new PlaceDTO(
                place.getPlaceId(),
                place.getPlaceName(),
                place.getMarking(),
                place.isCollectStatistics());
    }

    private List<TransitionDTO> createInnerTransitionDTOs(PetriObject petriObject) {

        List<TransitionDTO> innerTransitionDTOs = new ArrayList<>();

        for (Transition transition : petriObject) {

            TransitionDTO transitionDTO = createInnerTransitionDTO(transition);

            innerTransitionDTOs.add(transitionDTO);
        }

        return innerTransitionDTOs;
    }

    private TransitionDTO createInnerTransitionDTO(Transition transition) {

        long transitionId = transition.getTransitionId();
        String transitionName = transition.getTransitionName();
        int priority = transition.getPriority();
        double probability = transition.getProbability();
        DelayGeneratorDTO delayGeneratorDTO = createDelayGeneratorDTO(transition.getDelayGenerator());
        boolean collectStatistics = transition.isCollectStatistics();
        List<ArcInDTO> arcsInDTOs = createArcsIn(transition.getArcsIn());
        List<ArcDTO> innerArcsOutDTOs = createInnerArcsOut(transition.getArcsOut());

        return new TransitionDTO(
                transitionId,
                transitionName,
                priority,
                probability,
                delayGeneratorDTO,
                collectStatistics,
                arcsInDTOs,
                innerArcsOutDTOs);
    }

    private DelayGeneratorDTO createDelayGeneratorDTO(DelayGenerator delayGenerator) {

        if (delayGenerator instanceof ConstantDelayGenerator) {

            ConstantDelayGenerator constantDelayGenerator = (ConstantDelayGenerator) delayGenerator;

            return new DelayGeneratorDTO(
                    DelayGeneratorTypeDTO.CONSTANT,
                    constantDelayGenerator.getConstantDelay());
        }

        if (delayGenerator instanceof UniformDelayGenerator) {

            UniformDelayGenerator uniformDelayGenerator = (UniformDelayGenerator) delayGenerator;

            return new DelayGeneratorDTO(
                    DelayGeneratorTypeDTO.UNIFORM,
                    uniformDelayGenerator.getMinDelay(),
                    uniformDelayGenerator.getMaxDelay());
        }

        if (delayGenerator instanceof NormalDelayGenerator) {

            NormalDelayGenerator normalDelayGenerator = (NormalDelayGenerator) delayGenerator;

            return new DelayGeneratorDTO(
                    DelayGeneratorTypeDTO.NORMAL,
                    normalDelayGenerator.getMeanDelay(),
                    normalDelayGenerator.getDelayDeviation());
        }

        if (delayGenerator instanceof ExponentialDelayGenerator) {

            ExponentialDelayGenerator exponentialDelayGenerator = (ExponentialDelayGenerator) delayGenerator;

            return new DelayGeneratorDTO(
                    DelayGeneratorTypeDTO.EXPONENTIAL,
                    exponentialDelayGenerator.getMeanDelay());
        }

        throw new PetriObjectModelCreatingException("Unrecognized delay generator class: " + delayGenerator.getClass().getName());
    }

    private List<ArcInDTO> createArcsIn(List<ArcIn> arcsIn) {

        List<ArcInDTO> arcsInDTOs = new ArrayList<>();

        for (ArcIn arcIn : arcsIn) {

            ArcInDTO arcInDTO = createArcInDTO(arcIn);

            arcsInDTOs.add(arcInDTO);
        }

        return arcsInDTOs;
    }

    private ArcInDTO createArcInDTO(ArcIn arcIn) {

        long placeId = arcIn.getPlace().getPlaceId();
        int multiplicity = arcIn.getMultiplicity();
        boolean informational = arcIn instanceof InformationalArcIn;

        return new ArcInDTO(placeId, multiplicity, informational);
    }

    private List<ArcDTO> createInnerArcsOut(List<ArcOut> arcsOut) {

        List<ArcDTO> innerArcsOut = new ArrayList<>();

        for (ArcOut arcOut : arcsOut) {

            if (arcOut.getPlace().getPetriObject() == arcOut.getTransition().getPetriObject()) {

                ArcDTO arcDTO = createArcDTO(arcOut);

                innerArcsOut.add(arcDTO);
            }
        }

        return innerArcsOut;
    }

    private ArcDTO createArcDTO(ArcOut arcOut) {
        return new ArcDTO(
                arcOut.getPlace().getPlaceId(),
                arcOut.getMultiplicity());
    }

    private Map<Long, List<ExternalArcDTO>> createExternalArcsDTOsMap(PetriObjectModel petriObjectModel) {

        Map<Long, List<ExternalArcDTO>> externalArcDTOsMap = new HashMap<>();

        for (PetriObject petriObject : petriObjectModel) {
            for (Transition transition : petriObject) {
                for (ArcOut arcOut : transition.getArcsOut()){

                    PetriObject fromPetriObject = arcOut.getTransition().getPetriObject();
                    PetriObject toPetriObject = arcOut.getPlace().getPetriObject();

                    if (fromPetriObject != toPetriObject) {

                        ExternalArcDTO externalArcDTO = createExternalArcsDTO(arcOut);

                        addExternalArcDTOToMap(externalArcDTOsMap, externalArcDTO, fromPetriObject);
                        addExternalArcDTOToMap(externalArcDTOsMap, externalArcDTO, toPetriObject);
                    }
                }
            }
        }

        return externalArcDTOsMap;
    }

    private ExternalArcDTO createExternalArcsDTO(ArcOut arcOut) {

        Transition fromTransition = arcOut.getTransition();
        Place toPlace = arcOut.getPlace();

        return new ExternalArcDTO(
                fromTransition.getPetriObject().getPetriObjectId(),
                fromTransition.getTransitionId(),
                toPlace.getPetriObject().getPetriObjectId(),
                toPlace.getPlaceId(),
                arcOut.getMultiplicity());
    }

    private void addExternalArcDTOToMap(
            Map<Long, List<ExternalArcDTO>> externalArcDTOsMap,
            ExternalArcDTO externalArcDTO,
            PetriObject petriObject) {

        for (long petriObjectId : petriObject.getPetriObjectIds()) {

            List<ExternalArcDTO> externalArcDTOs = externalArcDTOsMap.computeIfAbsent(petriObjectId, list -> new ArrayList<>());

            externalArcDTOs.add(externalArcDTO);
        }
    }
}
