package edu.pedorenko.petri.dto.statistics;

public class PlaceStatisticsDTO {

    private long marking;

    private long maxObservedMarking;

    private long minObservedMarking;

    private double meanMarking;

    public PlaceStatisticsDTO(
            long marking,
            long maxObservedMarking,
            long minObservedMarking,
            double meanMarking) {
        this.marking = marking;
        this.maxObservedMarking = maxObservedMarking;
        this.minObservedMarking = minObservedMarking;
        this.meanMarking = meanMarking;
    }

    public PlaceStatisticsDTO() {
    }

    public long getMarking() {
        return marking;
    }

    public void setMarking(long marking) {
        this.marking = marking;
    }

    public long getMaxObservedMarking() {
        return maxObservedMarking;
    }

    public void setMaxObservedMarking(long maxObservedMarking) {
        this.maxObservedMarking = maxObservedMarking;
    }

    public long getMinObservedMarking() {
        return minObservedMarking;
    }

    public void setMinObservedMarking(long minObservedMarking) {
        this.minObservedMarking = minObservedMarking;
    }

    public double getMeanMarking() {
        return meanMarking;
    }

    public void setMeanMarking(double meanMarking) {
        this.meanMarking = meanMarking;
    }

    public String toString() {
        return "PlaceStatisticsDTO{" +
                "marking=" + marking +
                ", maxObservedMarking=" + maxObservedMarking +
                ", minObservedMarking=" + minObservedMarking +
                ", meanMarking=" + meanMarking +
                '}';
    }
}
