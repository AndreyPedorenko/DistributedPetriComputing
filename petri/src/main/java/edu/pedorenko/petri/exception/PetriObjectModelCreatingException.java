package edu.pedorenko.petri.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PetriObjectModelCreatingException extends RuntimeException {

    public PetriObjectModelCreatingException(String message) {
        super(message);
    }
}
