package edu.pedorenko.petrinode.controller;

import edu.pedorenko.petri.dto.PreprocessedPetriObjectDTO;
import edu.pedorenko.petri.dto.statistics.PetriObjectStatisticsDTO;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObject;
import edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.ParallelComputingPetriObjectModel;
import edu.pedorenko.petrinode.model.computing_model.time.TimeState;
import edu.pedorenko.petrinode.model.statistics.PetriObjectModelStatistics;
import edu.pedorenko.petrinode.model.statistics.PetriObjectStatistics;
import edu.pedorenko.petrinode.service.PetriObjectCreatingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class AmqpController {

    private static final Logger logger = LoggerFactory.getLogger(AmqpController.class);

    private final PetriObjectCreatingService petriObjectCreatingService;

    private final ModelMapper modelMapper;

    private final ExecutorService executorService = Executors.newWorkStealingPool();

    @Autowired
    public AmqpController(
            PetriObjectCreatingService petriObjectCreatingService,
            ModelMapper modelMapper) {

        this.petriObjectCreatingService = petriObjectCreatingService;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = "#{environment.QUEUE_NAME}", concurrency = "100")
    public PetriObjectStatisticsDTO test(PreprocessedPetriObjectDTO petriObjectDTO) throws InterruptedException, ExecutionException {

        ParallelComputingPetriObjectModel petriObjectModel = petriObjectCreatingService.createPetriObjectModel(petriObjectDTO);
        ParallelComputingPetriObject parallelComputingPetriObject = petriObjectModel.getParallelComputingPetriObject(petriObjectDTO.getPetriObjectIds().get(0));
        parallelComputingPetriObject.setTimeState(new TimeState(petriObjectDTO.getTimeModelling()));
        Future future = executorService.submit(parallelComputingPetriObject);

        future.get();

        PetriObjectModelStatistics petriObjectModelStatistics = petriObjectModel.getPetriObjectModelStatistics();

        PetriObjectStatistics petriObjectStatisticsByPetriObjectId = petriObjectModelStatistics.getPetriObjectStatisticsByPetriObjectId(petriObjectDTO.getPetriObjectIds().get(0));

        PetriObjectStatisticsDTO petriObjectStatisticsDTO = convertToDTO(petriObjectStatisticsByPetriObjectId);
        System.out.println(petriObjectStatisticsDTO);


        return petriObjectStatisticsDTO;
    }


    private PetriObjectStatisticsDTO convertToDTO(PetriObjectStatistics petriObjectStatistics) {
        return modelMapper.map(petriObjectStatistics, PetriObjectStatisticsDTO.class);
    }
}
