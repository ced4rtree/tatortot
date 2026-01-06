package org.teamtators.tester.components;

import java.util.function.DoubleConsumer;
import org.teamtators.tester.ManualTest;

public class MotorTest extends ManualTest {
    private DoubleConsumer motorFunction;
    private double axisValue;
    private double multiplier;
    private double upperBound;
    private double lowerBound;

    public MotorTest(String name, DoubleConsumer motorFunction) {
        this(name, motorFunction, 1);
    }

    public MotorTest(String name, DoubleConsumer motorFunction, double multiplier) {
        this(name, motorFunction, multiplier, 1, -1);
    }

    public MotorTest(
            String name,
            DoubleConsumer motorFunction,
            double multiplier,
            double upperBound,
            double lowerBound) {
        super(name);
        this.motorFunction = motorFunction;
        this.multiplier = multiplier;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override
    public void start() {
        printTestInstructions("Push left joystick up or down to drive motor");

        axisValue = 0;
    }

    private double getSpeed() {
        double speed = axisValue * multiplier;
        speed = Math.min(speed, upperBound);
        speed = Math.max(speed, lowerBound);
        return speed;
    }

    @Override
    public void update() {
        motorFunction.accept(getSpeed());
    }

    @Override
    public void stop() {
        motorFunction.accept(0);
    }

    @Override
    public void updateLeftAxis(double value) {
        axisValue = value;
    }
}
