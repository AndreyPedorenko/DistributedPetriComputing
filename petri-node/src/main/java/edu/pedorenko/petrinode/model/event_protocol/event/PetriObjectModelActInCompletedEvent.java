package edu.pedorenko.petrinode.model.event_protocol.event;

import edu.pedorenko.petrinode.model.event_protocol.state_model.PetriObjectModelState;

public class PetriObjectModelActInCompletedEvent extends PetriObjectModelStateChangedEvent {
    public PetriObjectModelActInCompletedEvent(PetriObjectModelState petriObjectModelState, double currentTime) {
        super(petriObjectModelState, currentTime);
    }
}
