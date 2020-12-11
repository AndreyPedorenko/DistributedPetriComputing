package edu.pedorenko.petri.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class PetriObjectModelDTO {

    @NotEmpty
    private List<@Valid PetriObjectDTO> petriObjects;

    @NotEmpty
    private List<@Valid ExternalArcDTO> externalArcs;

    @NotNull
    @Positive
    private double timeModelling;

    public PetriObjectModelDTO(
            @NotEmpty List<@Valid PetriObjectDTO> petriObjects,
            @NotEmpty List<@Valid ExternalArcDTO> externalArcs,
            @NotNull @Positive double timeModelling) {

        this.petriObjects = petriObjects;
        this.externalArcs = externalArcs;
        this.timeModelling = timeModelling;
    }

    public PetriObjectModelDTO() {
    }

    public List<PetriObjectDTO> getPetriObjects() {
        return petriObjects;
    }

    public void setPetriObjects(List<PetriObjectDTO> petriObjects) {
        this.petriObjects = petriObjects;
    }

    public List<ExternalArcDTO> getExternalArcs() {
        return externalArcs;
    }

    public void setExternalArcs(List<ExternalArcDTO> externalArcs) {
        this.externalArcs = externalArcs;
    }

    public double getTimeModelling() {
        return timeModelling;
    }

    public void setTimeModelling(double timeModelling) {
        this.timeModelling = timeModelling;
    }

    public String toString() {
        return "PetriObjectModelDTO{" +
                "petriObjects=" + petriObjects +
                ", externalArcs=" + externalArcs +
                ", timeModelling=" + timeModelling +
                '}';
    }
}
