package edu.pedorenko.petri.dto.statistics;

import java.util.HashMap;
import java.util.Map;

public class PetriObjectStatisticsDTO {

    private Map<Long, TransitionStatisticsDTO> transitionsStatistics;

    private Map<Long, PlaceStatisticsDTO> placesStatistics;

    public PetriObjectStatisticsDTO(
            Map<Long, TransitionStatisticsDTO> transitionsStatistics,
            Map<Long, PlaceStatisticsDTO> placesStatistics) {
        this.transitionsStatistics = transitionsStatistics;
        this.placesStatistics = placesStatistics;
    }

    public PetriObjectStatisticsDTO() {
        transitionsStatistics = new HashMap<>();
        placesStatistics = new HashMap<>();
    }

    public Map<Long, TransitionStatisticsDTO> getTransitionsStatistics() {
        return transitionsStatistics;
    }

    public void setTransitionsStatistics(Map<Long, TransitionStatisticsDTO> transitionsStatistics) {
        this.transitionsStatistics = transitionsStatistics;
    }

    public Map<Long, PlaceStatisticsDTO> getPlacesStatistics() {
        return placesStatistics;
    }

    public void setPlacesStatistics(Map<Long, PlaceStatisticsDTO> placesStatistics) {
        this.placesStatistics = placesStatistics;
    }

    public String toString() {
        return "PetriObjectStatisticsDTO{" +
                "transitionsStatistics=" + transitionsStatistics +
                ", placesStatistics=" + placesStatistics +
                '}';
    }
}
