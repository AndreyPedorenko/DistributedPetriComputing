package edu.pedorenko.petrinode.model.model_computer.petri_object_merger;

import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;

class CreateArcInTask extends CreateArcTask {

    private boolean isInformational;

    CreateArcInTask(Transition transition, Place place, int multiplicity, boolean isInformational) {
        super(transition, place, multiplicity);
        this.isInformational = isInformational;
    }

    void doTask() {
        if (isInformational) {
            transition.addInformationalArcFrom(place, multiplicity);
        } else {
            transition.addArcFrom(place, multiplicity);
        }
    }
}
