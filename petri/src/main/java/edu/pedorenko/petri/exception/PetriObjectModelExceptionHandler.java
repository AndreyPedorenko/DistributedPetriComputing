package edu.pedorenko.petri.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Date;

@ControllerAdvice
public class PetriObjectModelExceptionHandler {

    @MessageExceptionHandler(
            {
                    PetriObjectModelCreatingException.class,
                    MethodArgumentNotValidException.class,
                    HttpMessageNotReadableException.class,
                    IllegalArgumentException.class
            })
    @SendToUser("/topic/statistics")
    public ResponseEntity<JsonResponse> handleCreatingExceptions(Exception ex) {
        return new ResponseEntity<>(
                new JsonResponse(
                        new Date(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler({ParallelExecutionException.class})
    @SendToUser("/topic/statistics")
    public ResponseEntity<JsonResponse> handleParallelExecutionException(ParallelExecutionException ex) {
        return new ResponseEntity<>(
                new JsonResponse(
                        new Date(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private class JsonResponse {

        Date timestamp;

        int status;

        String error;

        String message;

        public JsonResponse() {
        }

        public JsonResponse(Date timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
