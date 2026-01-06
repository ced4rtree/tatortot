package org.teamtators.tester.components;

import edu.wpi.first.util.function.BooleanConsumer;
import org.teamtators.tester.ManualTest;
import org.teamtators.util.XBOXController;

public class BinarySwitchTest extends ManualTest {
    private BooleanConsumer switchFunction;

    public BinarySwitchTest(String name, BooleanConsumer switchFunction) {
        super(name);
        this.switchFunction = switchFunction;
    }

    @Override
    public void start() {
        printTestInstructions("Push A to enable/set to true and B to disable/set to false");
    }

    @Override
    public void onButtonDown(XBOXController.Button button) {
        switch (button) {
            case kA -> {
                printTestInfo("Output set to TRUE.");
                switchFunction.accept(true);
            }
            case kB -> {
                printTestInfo("Output set to false.");
                switchFunction.accept(false);
            }
            default -> printTestInfo("Unknown button " + button.toString());
        }
    }
}
