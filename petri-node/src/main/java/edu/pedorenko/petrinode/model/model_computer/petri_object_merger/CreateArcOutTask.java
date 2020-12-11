package edu.pedorenko.petrinode.model.model_computer.petri_object_merger;

import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;

class CreateArcOutTask extends CreateArcTask {
    CreateArcOutTask(Transition transition, Place place, int multiplicity) {
        super(transition, place, multiplicity);
    }

    void doTask() {
        transition.addArcTo(place, multiplicity);
    }
}
