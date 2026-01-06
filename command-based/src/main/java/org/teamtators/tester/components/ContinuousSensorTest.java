package org.teamtators.tester.components;

import java.util.function.DoubleSupplier;
import org.teamtators.tester.ManualTest;
import org.teamtators.util.XBOXController;

public class ContinuousSensorTest extends ManualTest {
    private DoubleSupplier sensor;
    private double conversionFactor;

    public ContinuousSensorTest(String name, DoubleSupplier sensor) {
        this(name, sensor, 1.0);
    }

    public ContinuousSensorTest(String name, DoubleSupplier sensor, double conversionFactor) {
        super(name);
        this.sensor = sensor;
        this.conversionFactor = conversionFactor;
    }

    @Override
    public void start() {
        printTestInstructions(
                "Press A to get value of the sensor.\nPress B to get the value of the sensor in converted units");
    }

    @Override
    public void onButtonDown(XBOXController.Button button) {
        double value = sensor.getAsDouble();
        if (button == XBOXController.Button.kA) {
            System.out.println("Value: " + value);
        } else if (button == XBOXController.Button.kB) {
            System.out.println("Converted Value: " + (value * conversionFactor));
        }
    }
}
