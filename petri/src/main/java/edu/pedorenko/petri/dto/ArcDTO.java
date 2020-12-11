package edu.pedorenko.petri.dto;

import javax.validation.constraints.Positive;

public class ArcDTO {

    @Positive
    private long placeId;

    @Positive
    private int multiplicity = 1;

    public ArcDTO(
            @Positive long placeId,
            @Positive int multiplicity) {

        this.placeId = placeId;
        this.multiplicity = multiplicity;
    }

    public ArcDTO() {
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public int getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(int multiplicity) {
        this.multiplicity = multiplicity;
    }
}
