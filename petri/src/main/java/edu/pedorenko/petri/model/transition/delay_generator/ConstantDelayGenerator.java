package edu.pedorenko.petri.model.transition.delay_generator;

public class ConstantDelayGenerator implements DelayGenerator {

    private double constantDelay;

    public ConstantDelayGenerator(double constantDelay) {

        if (constantDelay < 0) {
            throw new IllegalArgumentException("Constant delay can't be less then zero. constantDelay = " + constantDelay);
        }

        this.constantDelay = constantDelay;
    }

    public Double get() {
        return constantDelay;
    }

    public double getConstantDelay() {
        return constantDelay;
    }
}
