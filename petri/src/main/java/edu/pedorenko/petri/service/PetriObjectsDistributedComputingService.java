package edu.pedorenko.petri.service;

import edu.pedorenko.petri.dto.PetriObjectModelDTO;
import edu.pedorenko.petri.dto.PlaceDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectDTO;
import edu.pedorenko.petri.dto.PreprocessedPetriObjectModelDTO;
import edu.pedorenko.petri.dto.TransitionDTO;
import edu.pedorenko.petri.dto.statistics.PetriObjectModelStatisticsDTO;
import edu.pedorenko.petri.dto.statistics.PetriObjectStatisticsDTO;
import edu.pedorenko.petri.dto.statistics.PlaceStatisticsDTO;
import edu.pedorenko.petri.dto.statistics.TransitionStatisticsDTO;
import edu.pedorenko.petri.exception.ParallelExecutionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PetriObjectsDistributedComputingService {

    private final AmqpTemplate amqpTemplate;

    private final ExecutorService executorService = Executors.newWorkStealingPool();

    @Value("${petri.queue.names}")
    private String[] queuesNames;

    private AtomicInteger queueNamesCursor = new AtomicInteger(0);

    @Autowired
    public PetriObjectsDistributedComputingService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public PetriObjectModelStatisticsDTO computeDistributed(PetriObjectModelDTO petriObjectModelDTO, PreprocessedPetriObjectModelDTO preprocessedPetriObjectModelDTO) {

        Map<List<Long>, Future> futures = new HashMap<>();
        for (PreprocessedPetriObjectDTO petriObjectDTO : preprocessedPetriObjectModelDTO.getPetriObjects()) {
            final String queueName = getQueueName();
            futures.put(
                    petriObjectDTO.getPetriObjectIds(),
                    executorService.submit(() -> amqpTemplate.convertSendAndReceive(queueName, petriObjectDTO))
            );
        }

        Map<Long, PetriObjectStatisticsDTO> petriObjectsStatistics = new HashMap<>();
        for (List<Long> petriObjectIds : futures.keySet()) {

            Future future = futures.get(petriObjectIds);

            PetriObjectStatisticsDTO petriObjectStatisticsDTO;
            try {
                petriObjectStatisticsDTO = (PetriObjectStatisticsDTO) future.get();
            } catch (Exception ex) {
                throw new ParallelExecutionException(ex.getMessage());
            }

            for (long petriObjectId : petriObjectIds) {

                List<Long> statisticsPetriObjectPlacesIds = petriObjectModelDTO.getPetriObjects().stream()
                        .filter(petriObjectDTO -> petriObjectDTO.getPetriObjectId() == petriObjectId)
                        .flatMap(petriObjectDTO -> petriObjectDTO.getPlaces().stream())
                        .filter(PlaceDTO::isCollectStatistics)
                        .map(PlaceDTO::getPlaceId)
                        .collect(Collectors.toList());

                Map<Long, PlaceStatisticsDTO> placeStatisticsDTOs = new HashMap<>();

                for (long statisticsPetriObjectPlacesId : statisticsPetriObjectPlacesIds) {
                    PlaceStatisticsDTO placeStatisticsDTO = petriObjectStatisticsDTO.getPlacesStatistics().get(statisticsPetriObjectPlacesId);
                    placeStatisticsDTOs.put(statisticsPetriObjectPlacesId, placeStatisticsDTO);
                }

                List<Long> statisticsPetriObjectTransitionIds = petriObjectModelDTO.getPetriObjects().stream()
                        .filter(petriObjectDTO -> petriObjectDTO.getPetriObjectId() == petriObjectId)
                        .flatMap(petriObjectDTO -> petriObjectDTO.getTransitions().stream())
                        .filter(TransitionDTO::isCollectStatistics)
                        .map(TransitionDTO::getTransitionId)
                        .collect(Collectors.toList());

                Map<Long, TransitionStatisticsDTO> transitionStatisticsDTOs = new HashMap<>();
                for (long statisticsPetriObjectTransitionId : statisticsPetriObjectTransitionIds) {
                    TransitionStatisticsDTO transitionStatisticsDTO = petriObjectStatisticsDTO.getTransitionsStatistics().get(statisticsPetriObjectTransitionId);
                    transitionStatisticsDTOs.put(statisticsPetriObjectTransitionId, transitionStatisticsDTO);
                }

                PetriObjectStatisticsDTO postprocessedPetriObjectStatisticsDTO = new PetriObjectStatisticsDTO(transitionStatisticsDTOs, placeStatisticsDTOs);
                petriObjectsStatistics.put(petriObjectId, postprocessedPetriObjectStatisticsDTO);
            }
        }

        return new PetriObjectModelStatisticsDTO(petriObjectsStatistics);
    }

    private String getQueueName() {
        return queuesNames[queueNamesCursor.getAndIncrement() % queuesNames.length];
    }
}
