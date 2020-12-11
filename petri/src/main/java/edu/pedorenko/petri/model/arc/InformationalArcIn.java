package edu.pedorenko.petri.model.arc;

import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;

public class InformationalArcIn extends ArcIn {

    public InformationalArcIn(Place place, Transition transition, int multiplicity) {
        super(place, transition, multiplicity);
    }
}
