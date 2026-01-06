package org.teamtators.util;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;

public class TatorPigeon extends Pigeon2 {
    private double offset = 0;

    public TatorPigeon(int deviceNumber, String canbus) {
        super(deviceNumber, canbus);
        System.out.println("Boot gyro angle: " + this.getYaw());
    }

    public TatorPigeon(int deviceNumber) {
        super(deviceNumber);
    }

    public void zero() {
        offset = -getYaw().getValueAsDouble();
    }

    public double getYawContinuous() {
        return getYaw().getValueAsDouble() + offset;
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getYawContinuous());
    }

    public void changeOffset(double degrees) {
        offset = -getYaw().getValueAsDouble() + degrees;
    }

    public void setCurrentAngle(double angle) {
        offset = angle;
    }
}
