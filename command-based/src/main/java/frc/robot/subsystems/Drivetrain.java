package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

public class Drivetrain { // extends SubsystemBase {
    // private CANSparkMax driveLeft;
    // private CANSparkMax driveRight;
    // private DifferentialDrive diffDrive;
    // private final Pigeon2 gyro;
    // private double initialAngle = 0;
    //
    // private DifferentialDriveOdometry odometry;
    //
    // private PIDController pidController;
    //
    // private final double ROTATIONS_PER_METER = 1;
    //
    // public Drivetrain() {
    // driveLeft = new CANSparkMax(33, MotorType.kBrushless);
    // driveRight = new CANSparkMax(21, MotorType.kBrushless);
    // driveLeft.setIdleMode(IdleMode.kBrake);
    // driveRight.setIdleMode(IdleMode.kBrake);
    // diffDrive = new DifferentialDrive(driveLeft, driveRight);
    // driveRight.setInverted(true);
    // gyro = new Pigeon2(0);
    //
    // driveLeft.getEncoder().setPosition(0);
    // driveRight.getEncoder().setPosition(0);
    //
    // pidController = new PIDController(0, 0, 0);
    //
    // odometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);
    // }
    //
    // @Override
    // public void periodic() {
    // odometry.update(
    // new Rotation2d(getYaw()),
    // rotationsToMeters(driveLeft.getEncoder().getPosition()),
    // rotationsToMeters(driveRight.getEncoder().getPosition()));
    // }
    //
    // private double rotationsToMeters(double rotations) {
    // return rotations / ROTATIONS_PER_METER;
    // }
    //
    // private void setSpeeds(double forward, double rotation) {
    //// negatives to reverse forward direction so that forward is the RSL
    // diffDrive.arcadeDrive(-forward, -rotation);
    // }
    //
    // public Command drive(DoubleSupplier forward, DoubleSupplier rotation) {
    // return this.run(() -> setSpeeds(forward.getAsDouble(), rotation.getAsDouble()))
    // .alongWith(Commands.print("am here"));
    // }
    //
    // public Command turn(Double setpoint) {
    // return this.run(
    // () -> {
    // double speed = pidController.calculate(getYaw(), initialAngle);
    // setSpeeds(-speed, speed);
    // })
    // .until(() -> Math.abs(getYaw() - initialAngle) >= setpoint)
    // .beforeStarting(
    // () -> {
    // initialAngle = getYaw();
    // double setpointDeg = setpoint;
    // pidController = new PIDController(.5 / setpointDeg, .2 / setpointDeg, 0);
    // });
    // }
    //
    // public Pose2d getOdometryPose() {
    // return odometry.getPoseMeters();
    // }
    //
    // public double getYaw() {
    // return gyro.getYaw().getValue();
    // }
    //
    // public void resetGyro() {
    // gyro.setYaw(0);
    // }
}
