package edu.pedorenko.petri.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class PreprocessedPetriObjectModelDTO {

    @NotEmpty
    private List<@Valid PreprocessedPetriObjectDTO> petriObjects;

    public PreprocessedPetriObjectModelDTO(
            @NotEmpty List<@Valid PreprocessedPetriObjectDTO> petriObjects) {

        this.petriObjects = petriObjects;
    }

    public PreprocessedPetriObjectModelDTO() {
    }

    public List<PreprocessedPetriObjectDTO> getPetriObjects() {
        return petriObjects;
    }

    public void setPetriObjects(List<PreprocessedPetriObjectDTO> petriObjects) {
        this.petriObjects = petriObjects;
    }

    public String toString() {
        return "PreprocessedPetriObjectModelDTO{" +
                "petriObjects=" + petriObjects +
                '}';
    }
}
