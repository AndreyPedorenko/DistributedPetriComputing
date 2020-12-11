package edu.pedorenko.petrinode.model.model_computer;

import edu.pedorenko.petrinode.model.computing_model.petri_object.arc.ComputingArcIn;
import edu.pedorenko.petrinode.model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petrinode.model.computing_model.petri_object.arc.ComputingInformationalArcIn;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObject;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObjectModel;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.ComputingExternalArcOut;
import edu.pedorenko.petrinode.model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petrinode.model.computing_model.petri_object.sequentional.SequentialComputingPetriObject;
import edu.pedorenko.petrinode.model.computing_model.petri_object.sequentional.SequentialComputingPetriObjectModel;
import edu.pedorenko.petrinode.model.computing_model.petri_object.transition.ComputingTransition;
import edu.pedorenko.petrinode.model.event_protocol.EventProtocol;
import edu.pedorenko.petrinode.model.event_protocol.EventProtocolFactory;
import edu.pedorenko.petrinode.model.model.PetriObject;
import edu.pedorenko.petrinode.model.model.PetriObjectModel;
import edu.pedorenko.petrinode.model.model.PetriObjectModelException;
import edu.pedorenko.petrinode.model.model.arc.ArcIn;
import edu.pedorenko.petrinode.model.model.arc.ArcOut;
import edu.pedorenko.petrinode.model.model.arc.InformationalArcIn;
import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;
import edu.pedorenko.petrinode.model.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petrinode.model.model_computer.cycles_resolver.StronglyConnectedComponentsResolver;
import edu.pedorenko.petrinode.model.statistics.PetriObjectModelStatistics;
import edu.pedorenko.petrinode.model.statistics.PetriObjectStatistics;
import edu.pedorenko.petrinode.model.statistics.PlaceStatistics;
import edu.pedorenko.petrinode.model.statistics.TransitionStatistics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class PetriObjectModelComputer {

    public static ParallelComputingPetriObjectModel createParallelComputingPetriObjectModel(long thisServerPOId, PetriObjectModel petriObjectModel, EventProtocolFactory eventProtocolFactory) {

        PetriObjectModelStatistics petriObjectModelStatistics = new PetriObjectModelStatistics();

        List<ParallelComputingPetriObject> parallelComputingPetriObjects
                = createParallelComputingPetriObjects(thisServerPOId, petriObjectModel, eventProtocolFactory, petriObjectModelStatistics);

        return new ParallelComputingPetriObjectModel(parallelComputingPetriObjects, petriObjectModelStatistics);
    }

    private static List<ParallelComputingPetriObject> createParallelComputingPetriObjects(
            long thisServerPOId,
            PetriObjectModel petriObjectModel,
            EventProtocolFactory eventProtocolFactory,
            PetriObjectModelStatistics petriObjectModelStatistics) {

        Map<Place, ComputingPlace> computingPlacesMap = new HashMap<>();
        Map<PetriObject, List<ComputingExternalArcOut>> nextObjectComputingExternalArcsOut = new HashMap<>();

        List<ParallelComputingPetriObject> parallelComputingPetriObjects = new ArrayList<>();

        for (PetriObject petriObject : petriObjectModel) {
            nextObjectComputingExternalArcsOut.put(petriObject, new ArrayList<>());
        }

        for (PetriObject petriObject : petriObjectModel) {

            PetriObjectStatistics petriObjectStatistics = new PetriObjectStatistics();

            Set<ComputingPlace> thisObjectComputingPlaces = new LinkedHashSet<>();

            List<CreateComputingExternalArcTask> createComputingExternalArcTasks = new ArrayList<>();

            List<ComputingTransition> computingTransitions = createParallelComputingTransitionsAndFillPlacesSet(
                    petriObject,
                    computingPlacesMap,
                    thisObjectComputingPlaces,
                    createComputingExternalArcTasks,
                    petriObjectStatistics);

            List<ComputingExternalArcOut> prevObjectsExternalArcsOut = nextObjectComputingExternalArcsOut.get(petriObject);

            List<Long> petriObjectIds = petriObject.getPetriObjectIds();
            String petriObjectName = petriObject.getPetriObjectName();
            int externalBufferSizeLimit = petriObject.getExternalBufferSizeLimit();

            ParallelComputingPetriObject computingPetriObject =
                    new ParallelComputingPetriObject(
                            petriObjectIds,
                            petriObjectName,
                            computingTransitions,
                            new ArrayList<>(thisObjectComputingPlaces),
                            prevObjectsExternalArcsOut,
                            externalBufferSizeLimit,
                            eventProtocolFactory.createEventProtocol(),
                            petriObjectStatistics);

            for (CreateComputingExternalArcTask createComputingExternalArcTask : createComputingExternalArcTasks) {

                ComputingTransition transition = createComputingExternalArcTask.getTransition();
                ComputingPlace place = createComputingExternalArcTask.getPlace();
                int multiplicity = createComputingExternalArcTask.getMultiplicity();

                ComputingExternalArcOut externalArcOut = new ComputingExternalArcOut(thisServerPOId, computingPetriObject, transition, place, multiplicity);

                transition.addArcOut(externalArcOut);
                computingPetriObject.addExternalArcOut(externalArcOut);

                PetriObject nextPetriObject = createComputingExternalArcTask.getNextPetriObject();
                nextObjectComputingExternalArcsOut.get(nextPetriObject).add(externalArcOut);
            }

            parallelComputingPetriObjects.add(computingPetriObject);

            if (petriObjectStatistics.hasStatistics()) {
                for (Long petriObjectId : petriObjectIds) {
                    petriObjectModelStatistics.putPetriObjectStatistics(petriObjectId, petriObjectStatistics);
                }
            }
        }

        return parallelComputingPetriObjects;
    }

    private static List<ComputingTransition> createParallelComputingTransitionsAndFillPlacesSet(
            PetriObject petriObject,
            Map<Place, ComputingPlace> computingPlacesMap,
            Set<ComputingPlace> thisObjectComputingPlaces,
            List<CreateComputingExternalArcTask> createComputingExternalArcTasks,
            PetriObjectStatistics petriObjectStatistics) {

        List<ComputingTransition> computingTransitions = new ArrayList<>();

        for (Transition transition : petriObject) {

            long transitionId = transition.getTransitionId();
            String transitionName = transition.getTransitionName();
            int priority = transition.getPriority();
            double probability = transition.getProbability();
            DelayGenerator delayGenerator = transition.getDelayGenerator();
            ComputingTransition computingTransition = new ComputingTransition(transitionId, transitionName, priority, probability, delayGenerator);

            addParallelArcsAndFillPlacesSet(
                    computingTransition,
                    transition,
                    computingPlacesMap,
                    thisObjectComputingPlaces,
                    createComputingExternalArcTasks,
                    petriObjectStatistics);

            computingTransitions.add(computingTransition);

            if (transition.isCollectStatistics()) {
                petriObjectStatistics.putTransitionStatistics(transitionId, new TransitionStatistics(computingTransition));
            }
        }

        return computingTransitions;
    }

    private static void addParallelArcsAndFillPlacesSet(
            ComputingTransition computingTransition,
            Transition transition,
            Map<Place, ComputingPlace> computingPlacesMap,
            Set<ComputingPlace> thisObjectComputingPlaces,
            List<CreateComputingExternalArcTask> createComputingExternalArcTasks,
            PetriObjectStatistics petriObjectStatistics) {

        for (ArcIn arcIn : transition.getArcsIn()) {

            Place place = arcIn.getPlace();

            if (place.getPetriObject() != transition.getPetriObject()) {//compare by link
                throw new IllegalStateException("Only external arcs out allowed in parallel Petri object model.\n" +
                        "External arc in: " + arcIn);
            }

            int multiplicity = arcIn.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, computingPlacesMap);
            if (place.getPetriObject() == transition.getPetriObject() && !thisObjectComputingPlaces.contains(computingPlace)) {
                thisObjectComputingPlaces.add(computingPlace);
                if (place.isCollectStatistics()) {
                    petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                }
            }

            if (arcIn instanceof InformationalArcIn) {
                computingTransition.addArcIn(
                        new ComputingInformationalArcIn(computingPlace, computingTransition, multiplicity));
            } else {
                computingTransition.addArcIn(
                        new ComputingArcIn(computingPlace, computingTransition, multiplicity));
            }
        }

        for (ArcOut arcOut : transition.getArcsOut()) {

            Place place = arcOut.getPlace();
            int multiplicity = arcOut.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, computingPlacesMap);

            if (place.getPetriObject() == transition.getPetriObject()) {//compare by link

                if (!thisObjectComputingPlaces.contains(computingPlace)) {
                    thisObjectComputingPlaces.add(computingPlace);
                    if (place.isCollectStatistics()) {
                        petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                    }
                }

                computingTransition.addArcOut(new ComputingArcOut(computingTransition, computingPlace, multiplicity));

            } else {

                PetriObject nextPetriObject = place.getPetriObject();

                createComputingExternalArcTasks.add(new CreateComputingExternalArcTask(
                        nextPetriObject,
                        computingTransition,
                        computingPlace,
                        multiplicity));
            }
        }
    }

    private static ComputingPlace createComputingPlace(Place place, Map<Place, ComputingPlace> allComputingPlacesMap) {

        ComputingPlace computingPlace;
        if (allComputingPlacesMap.containsKey(place)) {
            return allComputingPlacesMap.get(place);
        } else {
            long placeId = place.getPlaceId();
            String placeName = place.getPlaceName();
            int initialMarking = place.getMarking();
            computingPlace = new ComputingPlace(placeId, placeName, initialMarking);
            allComputingPlacesMap.put(place, computingPlace);
            return computingPlace;
        }
    }
}
