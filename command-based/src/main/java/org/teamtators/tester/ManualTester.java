package org.teamtators.tester;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.teamtators.util.JoystickModifiers;
import org.teamtators.util.XBOXController;

/** A class that allows manually running ManualTest's */
public class ManualTester extends Command {
    public static final XBOXController.Axis RIGHT_AXIS = XBOXController.Axis.kRIGHT_STICK_Y;
    public static final XBOXController.Axis LEFT_AXIS = XBOXController.Axis.kLEFT_STICK_Y;

    public static final double DEADZONE = 0.05;
    public static final double EXPONENT = 2.0;
    private int testGroupIndex = 0;
    private int testIndex = 0;
    private Timer timer = new Timer();

    private XBOXController controller;

    private Map<XBOXController.Button, Boolean> lastStates =
            new EnumMap<XBOXController.Button, Boolean>(XBOXController.Button.class);
    private List<ManualTestGroup> testGroups = new ArrayList<>();

    public ManualTester() {
        System.out.println("entering test mabob");
    }

    public void setController(XBOXController controller) {
        this.controller = controller;
    }

    @Override
    public void initialize() {
        System.out.println("==> Starting Manual Tester <==");
        // logger.info("==> Starting Manual Tester <==");
        System.out.println(
                "ManualTester has "
                        + testGroups.size()
                        + " groups and is currently at "
                        + testGroupIndex
                        + ", "
                        + testIndex);
        // logger.trace("ManualTester has " + testGroups.size() + " groups and is currently at " +
        // testGroupIndex + ", "
        //        + testIndex);
        if (controller == null) {
            System.out.println("Joystick must be set before using ManualTester");
            //            logger.error("Joystick must be set before using ManualTester");
            this.cancel();
        }
        lastStates.clear();
        beginTestGroup(testGroupIndex, testIndex);
        timer.start();
    }

    @Override
    public void execute() {
        timer.reset();
        timer.start();
        ManualTest test = getCurrentTest();
        if (test != null) {
            double rightAxisValue = -controller.getAxisValue(RIGHT_AXIS);
            double leftAxisValue = -controller.getAxisValue(LEFT_AXIS);

            rightAxisValue = JoystickModifiers.applyDriveModifiers(rightAxisValue, DEADZONE, EXPONENT);
            test.updateRightAxis(rightAxisValue);

            leftAxisValue = JoystickModifiers.applyDriveModifiers(leftAxisValue, DEADZONE, EXPONENT);
            test.updateLeftAxis(leftAxisValue);
        }
        for (XBOXController.Button button : XBOXController.Button.values()) {
            boolean value = controller.isButtonDown(button);
            Boolean lastValue = lastStates.get(button);
            if (lastValue == null) lastValue = false;
            if (value && !lastValue) {
                switch (button) {
                    case kPOV_DOWN:
                        nextTestGroup();
                        break;
                    case kPOV_UP:
                        previousTestGroup();
                        break;
                    case kPOV_RIGHT:
                        nextTest();
                        break;
                    case kPOV_LEFT:
                        previousTest();
                        break;
                    default:
                        if (test != null) {
                            test.onButtonDown(button);
                        }
                        break;
                }
            } else if (lastValue && !value) {
                if (test != null) {
                    test.onButtonUp(button);
                }
            }
            lastStates.put(button, value);
        }

        if (test != null) {
            test.update();
        }
    }

    @Override
    public void end(boolean interrupted) {
        ManualTest currentTest = getCurrentTest();
        if (currentTest != null) currentTest.stop();
        System.out.println("--> ManualTester finished <--");
        // logger.debug("--> ManualTester finished <--");
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private ManualTest getCurrentTest() {
        ManualTestGroup group = getCurrentTestGroup();
        if (group == null) return null;
        if (testIndex >= group.getTests().size()) return null;
        return group.getTests().get(testIndex);
    }

    private ManualTestGroup getCurrentTestGroup() {
        if (testGroupIndex >= testGroups.size()) return null;
        return testGroups.get(testGroupIndex);
    }

    private void stopTest() {
        ManualTest test = getCurrentTest();
        if (test != null) test.stop();
    }

    public void beginTest(int index) {
        if (getCurrentTestGroup() == null) return;
        stopTest();
        testIndex = index;
        startTest();
    }

    public void nextTest() {
        if (getCurrentTestGroup() == null) return;
        int newTestIndex = testIndex + 1;
        if (newTestIndex >= getCurrentTestGroup().getTests().size()) newTestIndex = 0;
        beginTest(newTestIndex);
    }

    public void previousTest() {
        if (getCurrentTestGroup() == null) return;
        int newTestIndex = testIndex - 1;
        if (getCurrentTestGroup().getTests().size() == 0) newTestIndex = 0;
        else if (newTestIndex < 0) newTestIndex = getCurrentTestGroup().getTests().size() - 1;
        beginTest(newTestIndex);
    }

    public void beginTestGroup(int index) {
        beginTestGroup(index, 0);
    }

    public void beginTestGroup(int groupIndex, int testIndex) {
        testGroupIndex = groupIndex;
        if (getCurrentTestGroup() == null) {
            System.out.println("--> There are no test groups <--");
            // logger.info("--> There are no test groups <--");
            return;
        }
        System.out.println("--> Entering Test Group " + getCurrentTestGroup().getName() + " <--");
        // logger.info("--> Entering Test Group '{}' <--", getCurrentTestGroup().getName());
        beginTest(testIndex);
    }

    public void nextTestGroup() {
        if (testGroups.isEmpty()) return;
        int nextGroupIndex = testGroupIndex + 1;
        if (nextGroupIndex >= testGroups.size()) nextGroupIndex = 0;
        beginTestGroup(nextGroupIndex);
    }

    public void previousTestGroup() {
        if (testGroups.isEmpty()) return;
        int nextGroupIndex = testGroupIndex - 1;
        if (nextGroupIndex < 0) nextGroupIndex = testGroups.size() - 1;
        beginTestGroup(nextGroupIndex);
    }

    private void startTest() {
        ManualTest test = getCurrentTest();
        if (test == null) {
            System.out.println("-> Test group " + getCurrentTestGroup().getName() + " is empty! <-");
            // logger.info("-> Test group '{}' is empty! <-", getCurrentTestGroup().getName());
        } else {
            System.out.println("-> Testing " + test.getName() + " <-");
            // logger.info("-> Testing '{}' <-", test.getName());
            test.start();
        }
    }

    /**
     * Register a new test group
     *
     * @param group the test group to register
     */
    public void registerTestGroup(ManualTestGroup group) {
        testGroups.add(group);
    }

    public void unregisterTestGroup(ManualTestGroup group) {
        testGroups.remove(group);
    }

    public void clearTestGroups() {
        testGroups.clear();
    }
}
