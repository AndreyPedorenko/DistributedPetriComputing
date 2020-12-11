package edu.pedorenko.petri.model.arc;

import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;

public class ArcIn extends Arc {

    public ArcIn(Place place, Transition transition, int multiplicity) {

        super(place.getPlaceName() + "|" + transition.getTransitionName(), place, transition, multiplicity);
    }
}
