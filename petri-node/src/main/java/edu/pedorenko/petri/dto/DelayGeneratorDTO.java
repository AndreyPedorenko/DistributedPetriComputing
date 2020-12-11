package edu.pedorenko.petri.dto;

import javax.validation.constraints.NotNull;

public class DelayGeneratorDTO {

    @NotNull
    private DelayGeneratorTypeDTO type;

    @NotNull
    private double firstParam;

    private double secondParam;

    public DelayGeneratorDTO(
            @NotNull DelayGeneratorTypeDTO type,
            @NotNull double firstParam) {

        this.type = type;
        this.firstParam = firstParam;
    }

    public DelayGeneratorDTO(
            @NotNull DelayGeneratorTypeDTO type,
            @NotNull double firstParam,
            double secondParam) {

        this.type = type;
        this.firstParam = firstParam;
        this.secondParam = secondParam;
    }

    public DelayGeneratorDTO() {
    }

    public DelayGeneratorTypeDTO getType() {
        return type;
    }

    public void setType(DelayGeneratorTypeDTO type) {
        this.type = type;
    }

    public double getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(double firstParam) {
        this.firstParam = firstParam;
    }

    public double getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(double secondParam) {
        this.secondParam = secondParam;
    }
}
