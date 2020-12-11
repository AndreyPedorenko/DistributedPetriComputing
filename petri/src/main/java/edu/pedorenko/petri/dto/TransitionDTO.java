package edu.pedorenko.petri.dto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class TransitionDTO {

    @Positive
    private long transitionId;

    @NotNull
    @NotEmpty
    private String transitionName;

    @PositiveOrZero
    private int priority;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private double probability = 1;

    @NotNull
    private DelayGeneratorDTO delayGenerator;

    private boolean collectStatistics;

    @NotEmpty
    private List<@Valid ArcInDTO> arcsIn;

    @NotNull
    private List<@Valid ArcDTO> arcsOut;

    public TransitionDTO(
            @Positive long transitionId,
            @NotNull @NotEmpty String transitionName,
            @PositiveOrZero int priority,
            @DecimalMin("0.0") @DecimalMax("1.0") double probability,
            DelayGeneratorDTO delayGenerator,
            boolean collectStatistics,
            @NotEmpty List<@Valid ArcInDTO> arcsIn,
            List<@Valid ArcDTO> arcsOut) {

        this.transitionId = transitionId;
        this.transitionName = transitionName;
        this.priority = priority;
        this.probability = probability;
        this.delayGenerator = delayGenerator;
        this.collectStatistics = collectStatistics;
        this.arcsIn = arcsIn;
        this.arcsOut = arcsOut;
    }

    public TransitionDTO() {
    }

    public long getTransitionId() {
        return transitionId;
    }

    public void setTransitionId(long transitionId) {
        this.transitionId = transitionId;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public DelayGeneratorDTO getDelayGenerator() {
        return delayGenerator;
    }

    public void setDelayGenerator(DelayGeneratorDTO delayGenerator) {
        this.delayGenerator = delayGenerator;
    }

    public boolean isCollectStatistics() {
        return collectStatistics;
    }

    public void setCollectStatistics(boolean collectStatistics) {
        this.collectStatistics = collectStatistics;
    }

    public List<ArcInDTO> getArcsIn() {
        return arcsIn;
    }

    public void setArcsIn(List<ArcInDTO> arcsIn) {
        this.arcsIn = arcsIn;
    }

    public List<ArcDTO> getArcsOut() {
        return arcsOut;
    }

    public void setArcsOut(List<ArcDTO> arcsOut) {
        this.arcsOut = arcsOut;
    }
}
