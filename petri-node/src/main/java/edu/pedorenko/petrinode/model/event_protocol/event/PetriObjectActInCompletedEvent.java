package edu.pedorenko.petrinode.model.event_protocol.event;

import edu.pedorenko.petrinode.model.event_protocol.state_model.PetriObjectState;

public class PetriObjectActInCompletedEvent extends PetriObjectStateChangedEvent {
    public PetriObjectActInCompletedEvent(PetriObjectState petriObjectState, double currentTime) {
        super(petriObjectState, currentTime);
    }
}
