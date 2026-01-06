package org.teamtators.util;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.GenericHID;

public class XBOXController extends GenericHID {

    public Button button;
    public static Axis axis;

    private double triggerAxisBarrier = 0.1;

    private final double triggerBooleanBuffer = .1;
    private final double booleanPOVButtonBarrier = 20;

    /**
     * Construct an instance of a controller.
     *
     * @param port The port index on the Driver Station that the controller is plugged into.
     */
    public XBOXController(final int port) {
        super(port);

        HAL.report(tResourceType.kResourceType_XboxController, port + 1);
    }

    public Class<Button> getButtonClass() {
        return Button.class;
    }

    public Class<Axis> getAxisClass() {
        return Axis.class;
    }

    /** Represents a digital button on an XboxController. */
    public enum Button {
        kA(1),
        kB(2),
        kX(3),
        kY(4),
        kBUMPER_LEFT(5),
        kBUMPER_RIGHT(6),
        kBACK(7),
        kSTART(8),
        kSTICK_LEFT(9),
        kSTICK_RIGHT(10),
        kTRIGGER_LEFT(11),
        kTRIGGER_RIGHT(12),
        kPOV_UP(13),
        kPOV_DOWN(14),
        kPOV_LEFT(15),
        kPOV_RIGHT(16);

        @SuppressWarnings("MemberName")
        public final int value;

        Button(int value) {
            this.value = value;
        }

        /**
         * Get the human-friendly name of the button, matching the relevant methods. This is done by
         * stripping the leading `k`, and if not a Bumper button append `Button`.
         *
         * <p>Primarily used for automated unit tests.
         *
         * @return the human-friendly name of the button.
         */
        @Override
        public String toString() {
            var name = this.name().substring(1); // Remove leading `k`
            return name;
        }
    }

    /** Represents an axis on an XboxController. */
    public enum Axis {
        kLEFT_STICK_X(0),
        kLEFT_STICK_Y(1),
        kLEFT_TRIGGER(2),
        kRIGHT_TRIGGER(3),
        kRIGHT_STICK_X(4),
        kRIGHT_STICK_Y(5);

        @SuppressWarnings("MemberName")
        public final int value;

        Axis(int value) {
            this.value = value;
        }

        /**
         * Get the human-friendly name of the axis, matching the relevant methods. This is done by
         * stripping the leading `k`, and if a trigger axis append `Axis`.
         *
         * <p>Primarily used for automated unit tests.
         *
         * @return the human-friendly name of the axis.
         */
        @Override
        public String toString() {
            var name = this.name().substring(1); // Remove leading `k`
            return name;
        }
    }

    /** returns if the button is down */
    public boolean isButtonDown(Button button) {
        switch (button) {
            case kTRIGGER_LEFT:
                return getLeftTriggerAxis() >= triggerAxisBarrier;
            case kTRIGGER_RIGHT:
                return getRightTriggerAxis() >= triggerAxisBarrier;
            case kPOV_UP:
                return getPOV() == 0;
            case kPOV_RIGHT:
                return getPOV() == 90;
            case kPOV_DOWN:
                return getPOV() == 180;
            case kPOV_LEFT:
                return getPOV() == 270;
            default:
                return getRawButton(button.value);
        }
    }

    @Override
    public boolean getRawButton(int button) {
        if (button == Button.kTRIGGER_RIGHT.value) {
            return getRightTriggerAxis() > triggerAxisBarrier;
        } else if (button == Button.kTRIGGER_LEFT.value) {
            return getLeftTriggerAxis() > triggerAxisBarrier;
        } else if (button == Button.kPOV_UP.value) {
            return getPOV() == 0;
        } else if (button == Button.kPOV_RIGHT.value) {
            return getPOV() == 90;
        } else if (button == Button.kPOV_DOWN.value) {
            return getPOV() == 180;
        } else if (button == Button.kPOV_LEFT.value) {
            return getPOV() == 270;
        } else {
            return super.getRawButton(button);
        }
    }

    public double getAxisValue(Axis axis) {
        return getRawAxis(axis.value);
    }

    /**
     * Get the X axis value of left side of the controller.
     *
     * @return The axis value.
     */
    public double getLeftX() {
        return getRawAxis(Axis.kLEFT_STICK_X.value);
    }

    /**
     * Get the X axis value of right side of the controller.
     *
     * @return The axis value.
     */
    public double getRightX() {
        return getRawAxis(Axis.kRIGHT_STICK_X.value);
    }

    /**
     * Get the Y axis value of left side of the controller.
     *
     * @return The axis value.
     */
    public double getLeftY() {
        return getRawAxis(Axis.kLEFT_STICK_Y.value);
    }

    /**
     * Get the Y axis value of right side of the controller.
     *
     * @return The axis value.
     */
    public double getRightY() {
        return getRawAxis(Axis.kRIGHT_STICK_Y.value);
    }

    /**
     * Get the left trigger (LT) axis value of the controller. Note that this axis is bound to the
     * range of [0, 1] as opposed to the usual [-1, 1].
     *
     * @return The axis value.
     */
    public double getLeftTriggerAxis() {
        return getRawAxis(Axis.kLEFT_TRIGGER.value);
    }

    /**
     * Get the right trigger (RT) axis value of the controller. Note that this axis is bound to the
     * range of [0, 1] as opposed to the usual [-1, 1].
     *
     * @return The axis value.
     */
    public double getRightTriggerAxis() {
        return getRawAxis(Axis.kRIGHT_TRIGGER.value);
    }

    /**
     * Read the value of the left bumper (LB) button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getLeftBumper() {
        return getRawButton(Button.kBUMPER_LEFT.value);
    }

    /**
     * Read the value of the right bumper (RB) button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getRightBumper() {
        return getRawButton(Button.kBUMPER_RIGHT.value);
    }

    /**
     * Whether the left bumper (LB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getLeftBumperPressed() {
        return getRawButtonPressed(Button.kBUMPER_LEFT.value);
    }

    /**
     * Whether the right bumper (RB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRightBumperPressed() {
        return getRawButtonPressed(Button.kBUMPER_RIGHT.value);
    }

    /**
     * Whether the left bumper (LB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getLeftBumperReleased() {
        return getRawButtonReleased(Button.kBUMPER_LEFT.value);
    }

    /**
     * Whether the right bumper (RB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getRightBumperReleased() {
        return getRawButtonReleased(Button.kBUMPER_RIGHT.value);
    }

    /**
     * Read the value of the left stick button (LSB) on the controller.
     *
     * @return The state of the button.
     */
    public boolean getLeftStickButton() {
        return getRawButton(Button.kSTICK_LEFT.value);
    }

    /**
     * Read the value of the right stick button (RSB) on the controller.
     *
     * @return The state of the button.
     */
    public boolean getRightStickButton() {
        return getRawButton(Button.kSTICK_RIGHT.value);
    }

    /**
     * Whether the left stick button (LSB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getLeftStickButtonPressed() {
        return getRawButtonPressed(Button.kSTICK_LEFT.value);
    }

    /**
     * Whether the right stick button (RSB) was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRightStickButtonPressed() {
        return getRawButtonPressed(Button.kSTICK_RIGHT.value);
    }

    /**
     * Whether the left stick button (LSB) was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getLeftStickButtonReleased() {
        return getRawButtonReleased(Button.kSTICK_LEFT.value);
    }

    /**
     * Whether the right stick (RSB) button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getRightStickButtonReleased() {
        return getRawButtonReleased(Button.kSTICK_RIGHT.value);
    }

    /**
     * Read the value of the A button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getAButton() {
        return getRawButton(Button.kA.value);
    }

    /**
     * Whether the A button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getAButtonPressed() {
        return getRawButtonPressed(Button.kA.value);
    }

    /**
     * Whether the A button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getAButtonReleased() {
        return getRawButtonReleased(Button.kA.value);
    }

    /**
     * Read the value of the B button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getBButton() {
        return getRawButton(Button.kB.value);
    }

    /**
     * Whether the B button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBButtonPressed() {
        return getRawButtonPressed(Button.kB.value);
    }

    /**
     * Whether the B button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBButtonReleased() {
        return getRawButtonReleased(Button.kB.value);
    }

    /**
     * Read the value of the X button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getXButton() {
        return getRawButton(Button.kX.value);
    }

    /**
     * Whether the X button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getXButtonPressed() {
        return getRawButtonPressed(Button.kX.value);
    }

    /**
     * Whether the X button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getXButtonReleased() {
        return getRawButtonReleased(Button.kX.value);
    }

    /**
     * Read the value of the Y button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getYButton() {
        return getRawButton(Button.kY.value);
    }

    /**
     * Whether the Y button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getYButtonPressed() {
        return getRawButtonPressed(Button.kY.value);
    }

    /**
     * Whether the Y button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getYButtonReleased() {
        return getRawButtonReleased(Button.kY.value);
    }

    /**
     * Read the value of the back button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getBackButton() {
        return getRawButton(Button.kBACK.value);
    }

    /**
     * Whether the back button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBackButtonPressed() {
        return getRawButtonPressed(Button.kBACK.value);
    }

    /**
     * Whether the back button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBackButtonReleased() {
        return getRawButtonReleased(Button.kBACK.value);
    }

    /**
     * Read the value of the start button on the controller.
     *
     * @return The state of the button.
     */
    public boolean getStartButton() {
        return getRawButton(Button.kSTART.value);
    }

    /**
     * Whether the start button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getStartButtonPressed() {
        return getRawButtonPressed(Button.kSTART.value);
    }

    /**
     * Whether the start button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getStartButtonReleased() {
        return getRawButtonReleased(Button.kSTART.value);
    }

    public boolean getStickPOV_UP() {
        return getRawButton(Button.kPOV_UP.value);
    }

    public boolean getStickPOV_DOWN() {
        return getRawButton(Button.kPOV_DOWN.value);
    }

    public boolean getStickPOV_LEFT() {
        return getRawButton(Button.kPOV_LEFT.value);
    }

    public boolean getStickPOV_RIGHT() {
        return getRawButton(Button.kPOV_RIGHT.value);
    }

    public boolean getPOV_UPButtonPressed() {
        return getRawButtonPressed(Button.kPOV_UP.value);
    }

    public boolean getPOV_DOWNButtonPressed() {
        return getRawButtonPressed(Button.kPOV_DOWN.value);
    }

    public boolean getPOV_LEFTButtonPressed() {
        return getRawButtonPressed(Button.kPOV_LEFT.value);
    }

    public boolean getPOV_RIGHTButtonPressed() {
        return getRawButtonPressed(Button.kPOV_RIGHT.value);
    }

    public boolean getPOV_UPButtonReleased() {
        return getRawButtonReleased(Button.kPOV_UP.value);
    }

    public boolean getPOV_DOWNButtonReleased() {
        return getRawButtonReleased(Button.kPOV_DOWN.value);
    }

    public boolean getPOV_LEFTButtonReleased() {
        return getRawButtonReleased(Button.kPOV_LEFT.value);
    }

    public boolean getPOV_RIGHTButtonReleased() {
        return getRawButtonReleased(Button.kPOV_RIGHT.value);
    }
}
