package frc.robot.subsystems.drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    private final DriveTrainIO io;
    private final DifferentialDriveOdometry odometry;
    private PIDController pidController;
    private double initialAngle = 0.0;

    private static final double ROTATIONS_PER_METER = 1.0;

    // Constructor using IO abstraction
    public Drivetrain(DriveTrainIO io) {
        this.io = io;
        pidController = new PIDController(0.0, 0.0, 0.0);
        odometry = new DifferentialDriveOdometry(new Rotation2d(), 0.0, 0.0);
    }

    // Convert encoder rotations to meters
    private double rotationsToMeters(double rotations) {
        return rotations / ROTATIONS_PER_METER;
    }

    @Override
    public void periodic() {
        // Update odometry with current gyro yaw and encoder values
        odometry.update(
                new Rotation2d(Math.toRadians(io.getYaw())),
                rotationsToMeters(io.getLeftEncoderRotations()),
                rotationsToMeters(io.getRightEncoderRotations()));
    }

    // Get the robot pose from odometry
    public Pose2d getOdometryPose() {
        return odometry.getPoseMeters();
    }

    // Turn command using PID control
    public Command turn(double setpoint) {
        return this.run(
                        () -> {
                            double speed = pidController.calculate(io.getYaw(), initialAngle);
                            setSpeeds(-speed, speed);
                        })
                .until(() -> Math.abs(io.getYaw() - initialAngle) >= setpoint)
                .beforeStarting(
                        () -> {
                            initialAngle = io.getYaw();
                            pidController = new PIDController(0.0, 0.0, 0.0); // Keep gains zero for now
                        });
    }

    // Send speeds to drivetrain
    private void setSpeeds(double forward, double rotation) {
        io.setArcadeSpeeds(forward, rotation);
    }
}
// okay so we pretty much just kept odemtry and PIDController among a few other things in here,
// while all the hardware went into the IO interface
