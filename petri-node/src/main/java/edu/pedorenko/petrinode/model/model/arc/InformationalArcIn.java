package edu.pedorenko.petrinode.model.model.arc;

import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;

public class InformationalArcIn extends ArcIn {

    public InformationalArcIn(Place place, Transition transition, int multiplicity) {
        super(place, transition, multiplicity);
    }
}
