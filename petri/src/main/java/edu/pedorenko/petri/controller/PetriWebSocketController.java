package edu.pedorenko.petri.controller;

import edu.pedorenko.petri.dto.PetriObjectModelDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectModelDTO;
import edu.pedorenko.petri.dto.statistics.PetriObjectModelStatisticsDTO;
import edu.pedorenko.petri.exception.ParallelExecutionException;
import edu.pedorenko.petri.exception.PetriObjectModelCreatingException;
import edu.pedorenko.petri.model.PetriObjectModel;
import edu.pedorenko.petri.model.PetriObjectModelException;
import edu.pedorenko.petri.service.PetriObjectModelCreatingService;
import edu.pedorenko.petri.service.PetriObjectsDistributedComputingService;
import edu.pedorenko.petri.util.cycles_resolver.StronglyConnectedComponentsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class PetriWebSocketController {

    private final PetriObjectModelCreatingService petriObjectModelCreatingService;

    private final PetriObjectsDistributedComputingService petriObjectsDistributedComputingService;

    @Autowired
    public PetriWebSocketController(
            PetriObjectModelCreatingService petriObjectModelCreatingService,
            PetriObjectsDistributedComputingService petriObjectsDistributedComputingService) {

        this.petriObjectModelCreatingService = petriObjectModelCreatingService;
        this.petriObjectsDistributedComputingService = petriObjectsDistributedComputingService;
    }

    @MessageMapping("/compute/distributed")
    @SendToUser("/topic/statistics")
    public PetriObjectModelStatisticsDTO computePetriObjectModelDistributed(
        @Valid @RequestBody PetriObjectModelDTO petriObjectModelDTO) {

        PetriObjectModel petriObjectModel = petriObjectModelCreatingService.createPetriObjectModel(petriObjectModelDTO);

        PetriObjectModel preprocessedPetriObjectModel;
        try {
            preprocessedPetriObjectModel = StronglyConnectedComponentsResolver.resolveStronglyConnectedComponents(petriObjectModel);
        } catch (PetriObjectModelException ex) {
            throw new PetriObjectModelCreatingException(ex.getMessage());
        }

        PreprocessedPetriObjectModelDTO preprocessedPetriObjectModelDTO =
                petriObjectModelCreatingService.createPreprocessedPetriObjectModelDTO(
                        preprocessedPetriObjectModel,
                        petriObjectModelDTO.getTimeModelling());


        return petriObjectsDistributedComputingService.computeDistributed(petriObjectModelDTO, preprocessedPetriObjectModelDTO);
    }

    @MessageExceptionHandler(
            {
                    PetriObjectModelCreatingException.class,
                    MethodArgumentNotValidException.class,
                    HttpMessageNotReadableException.class,
                    IllegalArgumentException.class,
                    MessageConversionException.class
            })
    @SendToUser("/topic/statistics")
    public JsonResponse handleCreatingExceptions(Exception ex) {
        return new PetriWebSocketController.JsonResponse(
                        new Date(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage());
    }

    @MessageExceptionHandler({ParallelExecutionException.class})
    @SendToUser("/topic/statistics")
    public JsonResponse handleParallelExecutionException(ParallelExecutionException ex) {
        return new PetriWebSocketController.JsonResponse(
                        new Date(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getMessage());
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
