package edu.pedorenko.petrinode.model.computing_model.petri_object.parallel;

import edu.pedorenko.petrinode.model.computing_model.time.TimeState;
import edu.pedorenko.petrinode.model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelComputingPetriObjectModel {

    private Map<Long, ParallelComputingPetriObject> petriObjects = new HashMap<>();

    private PetriObjectModelStatistics petriObjectModelStatistics;

    public ParallelComputingPetriObjectModel(List<ParallelComputingPetriObject> petriObjects, PetriObjectModelStatistics petriObjectModelStatistics) {
        petriObjects.forEach(petriObject -> this.petriObjects.put(petriObject.getPetriObjectIds().get(0), petriObject));
        this.petriObjectModelStatistics = petriObjectModelStatistics;
    }

    public PetriObjectModelStatistics go(double simulationTime) throws ExecutionException, InterruptedException {

        petriObjects.values().forEach(petriObject -> petriObject.setTimeState(new TimeState(simulationTime)));

        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Future> futures = new ArrayList<>();
        for (ParallelComputingPetriObject petriObject : petriObjects.values()) {
            futures.add(executorService.submit(petriObject));
        }

        for (Future future : futures) {
            future.get();
        }

        executorService.shutdown();

        return petriObjectModelStatistics;
    }

    public ParallelComputingPetriObject getParallelComputingPetriObject(long petriObjectId) {
        return petriObjects.get(petriObjectId);
    }

    public PetriObjectModelStatistics getPetriObjectModelStatistics() {
        return petriObjectModelStatistics;
    }
}
