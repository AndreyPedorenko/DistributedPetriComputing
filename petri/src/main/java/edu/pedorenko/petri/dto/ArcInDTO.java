package edu.pedorenko.petri.dto;

import javax.validation.constraints.Positive;

public class ArcInDTO extends ArcDTO {

    private boolean informational;

    public ArcInDTO(
            @Positive long placeId,
            @Positive int multiplicity,
            boolean informational) {

        super(placeId, multiplicity);

        this.informational = informational;
    }

    public ArcInDTO() {
    }

    public boolean isInformational() {
        return informational;
    }

    public void setInformational(boolean informational) {
        this.informational = informational;
    }
}
