package edu.pedorenko.petri.exception;

public class ParallelExecutionException extends RuntimeException {

    public ParallelExecutionException(String message) {
        super(message);
    }
}
