package edu.pedorenko.petri.model.arc;

import edu.pedorenko.petri.model.place.Place;
import edu.pedorenko.petri.model.transition.Transition;

public class ArcOut extends Arc {

    public ArcOut(Transition transition, Place place, int multiplicity) {
        super(transition.getTransitionName() + "|" + place.getPlaceName(), place, transition, multiplicity);
    }
}
