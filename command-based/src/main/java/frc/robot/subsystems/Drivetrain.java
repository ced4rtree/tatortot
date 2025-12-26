package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class Drivetrain extends SubsystemBase {
    private SparkMax driveLeft;
    private SparkMax driveRight;
    private DifferentialDrive diffDrive;
    private final Pigeon2 gyro;
    private Angle initialAngle = Degrees.of(0);

    private DifferentialDriveOdometry odometry;

    private PIDController pidController;

    private final double ROTATIONS_PER_METER = 1;

    public Drivetrain() {
        driveLeft = new SparkMax(33, MotorType.kBrushless);
        driveRight = new SparkMax(21, MotorType.kBrushless);

        SparkBaseConfig leftConfig =
                new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(25);
        SparkBaseConfig rightConfig = leftConfig.inverted(true);

        driveLeft.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        driveRight.configure(
                rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        diffDrive = new DifferentialDrive(driveLeft, driveRight);

        gyro = new Pigeon2(0);

        driveLeft.getEncoder().setPosition(0);
        driveRight.getEncoder().setPosition(0);

        pidController = new PIDController(0, 0, 0);

        odometry = new DifferentialDriveOdometry(Rotation2d.kZero, 0, 0);
    }

    @Override
    public void periodic() {
        odometry.update(
                new Rotation2d(getYaw()),
                rotationsToMeters(driveLeft.getEncoder().getPosition()),
                rotationsToMeters(driveRight.getEncoder().getPosition()));
    }

    private double rotationsToMeters(double rotations) {
        return rotations / ROTATIONS_PER_METER;
    }

    private void setSpeeds(double forward, double rotation) {
        // negatives to reverse forward direction so that forward is the RSL
        diffDrive.arcadeDrive(-forward, -rotation);
    }

    public Command drive(DoubleSupplier forward, DoubleSupplier rotation) {
        return this.run(() -> setSpeeds(forward.getAsDouble(), rotation.getAsDouble()))
                .alongWith(Commands.print("am here"));
    }

    public Command turn(double setpoint) {
        return this.run(
                        () -> {
                            double speed =
                                    pidController.calculate(getYaw().in(Degrees), initialAngle.in(Degrees));
                            setSpeeds(-speed, speed);
                        })
                .until(() -> getYaw().minus(initialAngle).abs(Degrees) >= setpoint)
                .beforeStarting(
                        () -> {
                            initialAngle = getYaw();
                            double setpointDeg = setpoint;
                            pidController = new PIDController(.5 / setpointDeg, .2 / setpointDeg, 0);
                        });
    }

    public Pose2d getOdometryPose() {
        return odometry.getPoseMeters();
    }

    public Angle getYaw() {
        return gyro.getYaw().getValue();
    }

    public void resetGyro() {
        gyro.setYaw(0);
    }
}
