package edu.pedorenko.petri.util.petri_object_merger;

import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;

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
