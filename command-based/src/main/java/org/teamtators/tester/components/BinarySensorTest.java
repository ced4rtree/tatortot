package org.teamtators.tester.components;

import java.util.function.BooleanSupplier;
import org.teamtators.tester.ManualTest;
import org.teamtators.util.XBOXController;

public class BinarySensorTest extends ManualTest {
    private BooleanSupplier sensor;

    public BinarySensorTest(String name, BooleanSupplier sensor) {
        super(name);
        this.sensor = sensor;
    }

    @Override
    public void start() {
        printTestInstructions("Press A to get the sensor state");
    }

    @Override
    public void onButtonDown(XBOXController.Button button) {
        if (button == XBOXController.Button.kA) {
            System.out.println("Value: " + sensor.getAsBoolean());
        }
    }
}
