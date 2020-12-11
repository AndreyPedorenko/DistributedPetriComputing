package edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc;

import edu.pedorenko.petrinode.model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObject;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.event_times_buffer.DistributedEventTimeBuffer;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.event_times_buffer.EventTimesBuffer;
import edu.pedorenko.petrinode.model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petrinode.model.computing_model.petri_object.transition.ComputingTransition;

public class ComputingExternalArcOut extends ComputingArcOut {

    private EventTimesBuffer eventTimesBuffer;

    private ParallelComputingPetriObject prevObject;

    public ComputingExternalArcOut(
            long thisServerPOId,
            ParallelComputingPetriObject prevObject,
            ComputingTransition transition,
            ComputingPlace place,
            int multiplicity) {

        super(transition, place, multiplicity);

        boolean sender = false;
        if (prevObject.getPetriObjectIds().get(0) == thisServerPOId) {
            sender = true;
        }

        eventTimesBuffer = new DistributedEventTimeBuffer(arcId, sender);

        this.prevObject = prevObject;
    }

    public ComputingExternalArcOut(
            long thisServerPOId,
            ParallelComputingPetriObject prevObject,
            ComputingTransition transition,
            ComputingPlace place) {

        this(thisServerPOId, prevObject, transition, place, 1);
    }

    public double getNearestEventTime() {

        return eventTimesBuffer.getNearestTime();
    }

    public void runArc(double currentTime) {

        eventTimesBuffer.addTime(currentTime);
    }

    public void actOut(double currentTime) {

        if (currentTime != eventTimesBuffer.getNearestTime()) {
            return;
        }

        eventTimesBuffer.getAndRemoveNearestTime();

        place.increaseMarking(multiplicity);

        prevObject.signalEventTakenFromNextObjectBuffer();
    }

    public int getBufferSize() {
        return eventTimesBuffer.getBufferSize();
    }
}
