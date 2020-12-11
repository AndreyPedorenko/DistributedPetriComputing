package edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.event_times_buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelEventTimesBuffer implements EventTimesBuffer {

    private Lock lock = new ReentrantLock();
    private Condition emptyBufferCondition = lock.newCondition();

    private List<Double> eventTimesBuffer = new ArrayList<>();

    public double getNearestTime() {

        try {
            lock.lock();

            while (eventTimesBuffer.isEmpty()) {
                emptyBufferCondition.await();
            }

            return eventTimesBuffer.get(0);

        } catch (InterruptedException ex) {

            throw new RuntimeException("Exception occurred while waiting on emptyBufferCondition", ex);

        } finally {
            lock.unlock();
        }
    }

    public double getAndRemoveNearestTime() {
        try {
            lock.lock();

            getNearestTime();

            return eventTimesBuffer.remove(0);

        } finally {
            lock.unlock();
        }
    }

    public void addTime(double time) {
        try {
            lock.lock();
            eventTimesBuffer.add(time);
            emptyBufferCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public int getBufferSize() {
        try {
            lock.lock();
            return eventTimesBuffer.size();
        } finally {
            lock.unlock();
        }
    }
}
