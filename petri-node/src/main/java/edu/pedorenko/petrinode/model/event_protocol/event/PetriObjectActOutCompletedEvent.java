package edu.pedorenko.petrinode.model.event_protocol.event;

import edu.pedorenko.petrinode.model.event_protocol.state_model.PetriObjectState;

public class PetriObjectActOutCompletedEvent extends PetriObjectStateChangedEvent {

    public PetriObjectActOutCompletedEvent(PetriObjectState petriObjectState, double currentTime) {
        super(petriObjectState, currentTime);
    }
}
