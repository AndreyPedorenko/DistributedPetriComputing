package edu.pedorenko.petri.util.petri_object_merger;

import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;

class CreateArcOutTask extends CreateArcTask {
    CreateArcOutTask(Transition transition, Place place, int multiplicity) {
        super(transition, place, multiplicity);
    }

    void doTask() {
        transition.addArcTo(place, multiplicity);
    }
}
