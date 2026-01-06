package org.teamtators.tester.components;

import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import java.util.ArrayList;
import org.teamtators.constants.BlinkinRawColors;
import org.teamtators.tester.ManualTest;
import org.teamtators.util.XBOXController;

public class BlinkinColorTest extends ManualTest {
    private static ArrayList<Pair<String, Double>> allcolors =
            new ArrayList<Pair<String, Double>>(100);

    static {
        String actionName;
        for (var field : BlinkinRawColors.class.getDeclaredFields()) {
            try {
                actionName = field.getName();
                actionName = actionName.replace("_", " ").replaceAll("([a-z])([A-Z])", "$1 $2");
                allcolors.add(new Pair<String, Double>(actionName, field.getDouble(null)));
            } catch (Exception e) {
            }
        }
    }

    private PWMMotorController controller;
    private int colorInd = 0;

    public BlinkinColorTest(String name, PWMMotorController controller) {
        super(name);
        this.controller = controller;
    }

    @Override
    public void start() {
        printTestInstructions("Use the bumpers to switch colors");
    }

    @Override
    public void onButtonDown(XBOXController.Button button) {
        int oldColorInd = colorInd;
        if (button == XBOXController.Button.kBUMPER_LEFT) {
            colorInd -= 1;
            if (colorInd < 0) {
                colorInd = allcolors.size() - 1;
            }
        }
        if (button == XBOXController.Button.kBUMPER_RIGHT) {
            colorInd += 1;
            if (colorInd >= allcolors.size()) {
                colorInd = 0;
            }
        }
        if (oldColorInd != colorInd) {
            setColor(allcolors.get(colorInd));
        }
    }

    private void setColor(Pair<String, Double> color) {
        printTestInfo("Color: " + color.getFirst());
        controller.set(color.getSecond());
    }
}
