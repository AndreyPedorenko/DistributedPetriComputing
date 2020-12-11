package edu.pedorenko.petri.dto.statistics;

import java.util.Map;

public class PetriObjectModelStatisticsDTO {

    private Map<Long, PetriObjectStatisticsDTO> petriObjectStatistics;

    public PetriObjectModelStatisticsDTO(Map<Long, PetriObjectStatisticsDTO> petriObjectStatistics) {
        this.petriObjectStatistics = petriObjectStatistics;
    }

    public PetriObjectModelStatisticsDTO() {
    }

    public Map<Long, PetriObjectStatisticsDTO> getPetriObjectStatistics() {
        return petriObjectStatistics;
    }

    public void setPetriObjectStatistics(Map<Long, PetriObjectStatisticsDTO> petriObjectStatistics) {
        this.petriObjectStatistics = petriObjectStatistics;
    }
}
