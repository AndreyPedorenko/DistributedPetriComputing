package edu.pedorenko.petri.dto.statistics;

public class TransitionStatisticsDTO {

    private long bufferSize;

    private long maxObservedBufferSize;

    private long minObservedBufferSize;

    private double meanBufferSize;

    public TransitionStatisticsDTO(
            long bufferSize,
            long maxObservedBufferSize,
            long minObservedBufferSize,
            double meanBufferSize) {

        this.bufferSize = bufferSize;
        this.maxObservedBufferSize = maxObservedBufferSize;
        this.minObservedBufferSize = minObservedBufferSize;
        this.meanBufferSize = meanBufferSize;
    }

    public TransitionStatisticsDTO() {
    }

    public long getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
    }

    public long getMaxObservedBufferSize() {
        return maxObservedBufferSize;
    }

    public void setMaxObservedBufferSize(long maxObservedBufferSize) {
        this.maxObservedBufferSize = maxObservedBufferSize;
    }

    public long getMinObservedBufferSize() {
        return minObservedBufferSize;
    }

    public void setMinObservedBufferSize(long minObservedBufferSize) {
        this.minObservedBufferSize = minObservedBufferSize;
    }

    public double getMeanBufferSize() {
        return meanBufferSize;
    }

    public void setMeanBufferSize(double meanBufferSize) {
        this.meanBufferSize = meanBufferSize;
    }

    public String toString() {
        return "TransitionStatisticsDTO{" +
                "bufferSize=" + bufferSize +
                ", maxObservedBufferSize=" + maxObservedBufferSize +
                ", minObservedBufferSize=" + minObservedBufferSize +
                ", meanBufferSize=" + meanBufferSize +
                '}';
    }
}
