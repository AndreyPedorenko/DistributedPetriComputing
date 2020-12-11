package edu.pedorenko.petri.model;

public class PetriObjectModelException extends Exception {
    public PetriObjectModelException(String message) {
        super(message);
    }

    public PetriObjectModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
