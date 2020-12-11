package edu.pedorenko.petri.dto;

import javax.validation.constraints.Positive;

public class ExternalArcDTO {

    @Positive
    private long fromPetriObjectId;

    @Positive
    private long fromTransitionId;

    @Positive
    private long toPetriObjectId;

    @Positive
    private long toPlaceId;

    @Positive
    private int multiplicity = 1;

    public ExternalArcDTO(
            @Positive long fromPetriObjectId,
            @Positive long fromTransitionId,
            @Positive long toPetriObjectId,
            @Positive long toPlaceId,
            @Positive int multiplicity) {

        this.fromPetriObjectId = fromPetriObjectId;
        this.fromTransitionId = fromTransitionId;
        this.toPetriObjectId = toPetriObjectId;
        this.toPlaceId = toPlaceId;
        this.multiplicity = multiplicity;
    }

    public ExternalArcDTO() {
    }

    public long getFromPetriObjectId() {
        return fromPetriObjectId;
    }

    public void setFromPetriObjectId(long fromPetriObjectId) {
        this.fromPetriObjectId = fromPetriObjectId;
    }

    public long getFromTransitionId() {
        return fromTransitionId;
    }

    public void setFromTransitionId(long fromTransitionId) {
        this.fromTransitionId = fromTransitionId;
    }

    public long getToPetriObjectId() {
        return toPetriObjectId;
    }

    public void setToPetriObjectId(long toPetriObjectId) {
        this.toPetriObjectId = toPetriObjectId;
    }

    public long getToPlaceId() {
        return toPlaceId;
    }

    public void setToPlaceId(long toPlaceId) {
        this.toPlaceId = toPlaceId;
    }

    public int getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(int multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String toString() {
        return "ExternalArcDTO{" +
                "fromPetriObjectId=" + fromPetriObjectId +
                ", fromTransitionId=" + fromTransitionId +
                ", toPetriObjectId=" + toPetriObjectId +
                ", toPlaceId=" + toPlaceId +
                ", multiplicity=" + multiplicity +
                '}';
    }
}
