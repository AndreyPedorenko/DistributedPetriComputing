package edu.pedorenko.petrinode.model.model.arc;

import edu.pedorenko.petrinode.model.model.place.Place;
import edu.pedorenko.petrinode.model.model.transition.Transition;

public class ArcOut extends Arc {

    public ArcOut(Transition transition, Place place, int multiplicity) {
        super(transition.getTransitionName() + "|" + place.getPlaceName(), place, transition, multiplicity);
    }
}
