package edu.pedorenko.petri.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class PetriObjectDTO {

    @Positive
    private long petriObjectId;

    @NotNull
    @NotEmpty
    private String petriObjectName;

    @NotEmpty
    private List<@Valid PlaceDTO> places;

    @NotEmpty
    private List<@Valid TransitionDTO> transitions;

    public PetriObjectDTO(
            @Positive long petriObjectId,
            @NotNull @NotEmpty String petriObjectName,
            @NotEmpty List<@Valid PlaceDTO> places,
            @NotEmpty List<@Valid TransitionDTO> transitions) {

        this.petriObjectId = petriObjectId;
        this.petriObjectName = petriObjectName;
        this.places = places;
        this.transitions = transitions;
    }

    public PetriObjectDTO() {
    }

    public long getPetriObjectId() {
        return petriObjectId;
    }

    public void setPetriObjectId(long petriObjectId) {
        this.petriObjectId = petriObjectId;
    }

    public String getPetriObjectName() {
        return petriObjectName;
    }

    public void setPetriObjectName(String petriObjectName) {
        this.petriObjectName = petriObjectName;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceDTO> places) {
        this.places = places;
    }

    public List<TransitionDTO> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<TransitionDTO> transitions) {
        this.transitions = transitions;
    }

    public String toString() {
        return "PetriObjectDTO{" +
                "petriObjectId=" + petriObjectId +
                ", petriObjectName='" + petriObjectName + '\'' +
                ", places=" + places +
                ", transitions=" + transitions +
                '}';
    }
}
