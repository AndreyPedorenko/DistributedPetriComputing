package edu.pedorenko.petrinode.model.event_protocol;

public class DummyEventProtocolFactory implements EventProtocolFactory {

    public DummyEventProtocolFactory() {
    }

    public EventProtocol createEventProtocol() {
        return new DummyEventProtocol();
    }
}
