package org.teamtators.util;

/** Utilities for DriveTank input */
public class JoystickModifiers {
    public double offset = 0.0;
    public double deadzone = 0.0;
    public double exponent = 1.0;
    public double scale = 1.0;
    public double cutoffX = 0.8;
    public double cutoffY = 0.005;
    private double limitY = 1;
    private Vector2d returnVal = new Vector2d();

    public JoystickModifiers() {}

    public JoystickModifiers(double offset, double deadzone, double exponent, double scale) {
        this.offset = offset;
        this.deadzone = deadzone;
        this.exponent = exponent;
        this.scale = scale;
    }

    public JoystickModifiers(
            double offset, double deadzone, double exponent, double cutoffX, double cutoffY) {
        this.offset = offset;
        this.deadzone = deadzone;
        this.exponent = exponent;
        this.cutoffX = cutoffX;
        this.cutoffY = cutoffY;
    }

    public JoystickModifiers(
            double offset,
            double deadzone,
            double exponent,
            double cutoffX,
            double cutoffY,
            double limitY) {
        this.offset = offset;
        this.deadzone = deadzone;
        this.exponent = exponent;
        this.cutoffX = cutoffX;
        this.cutoffY = cutoffY;
        this.limitY = limitY;
    }

    public double applyExponential(double input) {
        input = input + offset;
        input = applyDeadzone(input, deadzone);
        input = signedExponent(input, exponent);
        input = input * scale;
        return input;
    }

    public static double applyDriveModifiers(double input, double deadzone, double exponent) {
        input = applyDeadzone(input, deadzone);
        return signedExponent(input, exponent);
    }

    private static double signedExponent(double input, double exponent) {
        double absolute = Math.abs(input);
        double sign = Math.signum(input);
        return sign * Math.pow(absolute, exponent);
    }

    public static double applyDeadzone(double input, double deadzone) {
        if (Math.abs(input) <= deadzone) return 0;
        return input * (1 - deadzone) + (deadzone * Math.signum(input));
    }

    /**
     * Adjust joystick controls (piecewise) to fit the circular nature of the joystick
     *
     * @param x
     * @param y
     * @return A {@link Vector2d} containing the corrected values.
     */
    public Vector2d radialAdjust(double x, double y) {

        returnVal.setX(0);
        returnVal.setY(0);

        if (x == 0 && y == 0) {
            // do nothing return zeros
            // Fast exit
        } else {

            double power = Math.min(Math.hypot(x, y), 1.0);
            double angle = Math.atan2(y, x);

            // Depending on which algorithm we want to use
            double adjustedPower = applyPiecewise(power);
            // double adjustedPower = applyExponential(power);

            if (adjustedPower == 0) {
                // do nothing return zeros
            } else {
                // Convert back from polar to cartesian
                returnVal = Vector2d.fromPolar(angle, adjustedPower);
            }
        }

        // Assign the return value
        return returnVal;
    }

    public double applyPiecewise(double input) {
        double out;
        double absInput = Math.abs(input);
        double sign = Math.signum(input);

        if (absInput < deadzone) out = 0;
        else if (absInput <= cutoffX) {
            out = sign * ((absInput - deadzone) / (cutoffX - deadzone)) * cutoffY;
        } else {
            out =
                    sign
                            * (cutoffY
                                    + ((1 - cutoffY) * Math.pow((absInput - cutoffX) / (1 - cutoffX), exponent)));
        }

        if (Math.abs(out) > limitY) out = sign * limitY;

        // System.out.println( "Joystick In : " + input + ", Out : " + out );
        return out;
    }
}
