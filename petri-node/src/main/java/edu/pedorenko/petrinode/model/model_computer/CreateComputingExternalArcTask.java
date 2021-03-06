package edu.pedorenko.petrinode.model.model_computer;

import edu.pedorenko.petrinode.model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petrinode.model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petrinode.model.computing_model.petri_object.transition.ComputingTransition;
import edu.pedorenko.petrinode.model.model.PetriObject;

class CreateComputingExternalArcTask extends ComputingArcOut {

    private PetriObject nextPetriObject;

    CreateComputingExternalArcTask(
            PetriObject nextPetriObject,
            ComputingTransition transition,
            ComputingPlace place,
            int multiplicity) {

        super(transition, place, multiplicity);
        this.nextPetriObject = nextPetriObject;
    }

    PetriObject getNextPetriObject() {
        return nextPetriObject;
    }
}
