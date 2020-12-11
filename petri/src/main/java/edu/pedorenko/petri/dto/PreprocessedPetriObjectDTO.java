package edu.pedorenko.petri.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class PreprocessedPetriObjectDTO {

    @NotEmpty
    private List<@Positive Long> petriObjectIds;

    @NotNull
    @NotEmpty
    private String petriObjectName;

    @NotEmpty
    private List<@Valid PlaceDTO> places;

    @NotEmpty
    private List<@Valid TransitionDTO> transitions;

    @NotEmpty
    private List<@Valid ExternalArcDTO> externalArcs;

    @NotNull
    @Positive
    private double timeModelling;

    public PreprocessedPetriObjectDTO(
            @NotEmpty List<@Positive Long> petriObjectIds,
            @NotNull @NotEmpty String petriObjectName,
            @NotEmpty List<@Valid PlaceDTO> places,
            @NotEmpty List<@Valid TransitionDTO> transitions,
            @NotEmpty List<@Valid ExternalArcDTO> externalArcs,
            @NotNull @Positive double timeModelling) {

        this.petriObjectIds = petriObjectIds;
        this.petriObjectName = petriObjectName;
        this.places = places;
        this.transitions = transitions;
        this.externalArcs = externalArcs;
        this.timeModelling = timeModelling;
    }

    public PreprocessedPetriObjectDTO() {
    }

    public List<Long> getPetriObjectIds() {
        return petriObjectIds;
    }

    public void setPetriObjectIds(List<Long> petriObjectIds) {
        this.petriObjectIds = petriObjectIds;
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
        return "PreprocessedPetriObjectDTO{" +
                "petriObjectIds=" + petriObjectIds +
                ", petriObjectName='" + petriObjectName + '\'' +
                ", places=" + places +
                ", transitions=" + transitions +
                ", externalArcs=" + externalArcs +
                ", timeModelling=" + timeModelling +
                '}';
    }
}
