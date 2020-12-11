package edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.event_times_buffer;

public interface EventTimesBuffer {

    double getNearestTime();

    double getAndRemoveNearestTime();

    void addTime(double time);

    int getBufferSize();
}
