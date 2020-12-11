package edu.pedorenko.petrinode.model.model.arc;

import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;

public class ArcIn extends Arc {

    public ArcIn(Place place, Transition transition, int multiplicity) {

        super(place.getPlaceName() + "|" + transition.getTransitionName(), place, transition, multiplicity);
    }
}
