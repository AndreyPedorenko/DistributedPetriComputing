package edu.pedorenko.petrinode.model.event_protocol;

import edu.pedorenko.petrinode.model.event_protocol.event.PetriObjectActInCompletedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.PetriObjectActOutCompletedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.PetriObjectModelActInCompletedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.PetriObjectModelActOutCompletedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.PetriObjectsConflictResolvedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.TimeMovedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.TransitionActInCompletedEvent;
import edu.pedorenko.petrinode.model.event_protocol.event.TransitionsConflictResolvedEvent;

public class DummyEventProtocol implements EventProtocol {

    public DummyEventProtocol() {
    }

    public boolean isEnabled() {
        return false;
    }

    public void onTimeMovedEvent(TimeMovedEvent timeMovedEvent) {
    }

    public void onTransitionActInCompletedEvent(TransitionActInCompletedEvent transitionActInCompletedEvent) {

    }

    public void onPetriObjectActOutCompletedEvent(PetriObjectActOutCompletedEvent petriObjectActOutCompletedEvent) {

    }

    public void onPetriObjectActInCompletedEvent(PetriObjectActInCompletedEvent petriObjectActInCompletedEvent) {

    }

    public void onPetriObjectModelActOutCompletedEvent(PetriObjectModelActOutCompletedEvent petriObjectModelActOutCompletedEvent) {

    }

    public void onPetriObjectModelActInCompletedEvent(PetriObjectModelActInCompletedEvent petriObjectModelActInCompletedEvent) {

    }

    public void onPetriObjectsConflictResolvedEvent(PetriObjectsConflictResolvedEvent petriObjectsConflictResolvedEvent) {

    }

    public void onTransitionsConflictResolvedEvent(TransitionsConflictResolvedEvent transitionsConflictResolvedEvent) {

    }
}
