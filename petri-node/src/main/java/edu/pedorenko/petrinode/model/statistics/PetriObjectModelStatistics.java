package edu.pedorenko.petrinode.model.statistics;

import java.util.HashMap;
import java.util.Map;

public class PetriObjectModelStatistics {

    private Map<Long, PetriObjectStatistics> petriObjectsStatistics = new HashMap<>();

    public PetriObjectModelStatistics() {
    }

    public void putPetriObjectStatistics(long petriObjectId, PetriObjectStatistics petriObjectStatistics) {
        petriObjectsStatistics.put(petriObjectId, petriObjectStatistics);
    }

    public PetriObjectStatistics getPetriObjectStatisticsByPetriObjectId(long petriObjectId) {
        return petriObjectsStatistics.get(petriObjectId);
    }

    public void doStatistics(double timeDelta) {
        for (PetriObjectStatistics petriObjectStatistics : petriObjectsStatistics.values()) {
            petriObjectStatistics.doStatistics(timeDelta);
        }
    }

    public Map<Long, PetriObjectStatistics> getPetriObjectsStatistics() {
        return petriObjectsStatistics;
    }

    public String toString() {
        return "PetriObjectModelStatistics{" +
                "petriObjectsStatistics=" + petriObjectsStatistics +
                '}';
    }
}
