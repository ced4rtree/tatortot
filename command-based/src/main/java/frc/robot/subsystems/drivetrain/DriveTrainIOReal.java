package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrainIOReal implements DriveTrainIO {

    private final CANSparkMax driveLeft;
    private final CANSparkMax driveRight;
    private final DifferentialDrive diffDrive;
    private final Pigeon2 gyro;

    public DriveTrainIOReal() {
        // Initialize hardware first
        driveLeft = new CANSparkMax(33, MotorType.kBrushless);
        driveRight = new CANSparkMax(21, MotorType.kBrushless);
        //idk what the deviceIds mean but shhhh

    

        // Configure motors
        driveLeft.setIdleMode(IdleMode.kBrake);
        driveRight.setIdleMode(IdleMode.kBrake);
        driveRight.setInverted(true);

        // Reset encoders
        driveLeft.getEncoder().setPosition(0);
        driveRight.getEncoder().setPosition(0);

        // Differential drive
        diffDrive = new DifferentialDrive(driveLeft, driveRight);

        // Gyro
        gyro = new Pigeon2(0);
        gyro.setYaw(0);
    }

    // Implement interface methods

    @Override
    public double getLeftEncoderRotations() {
        return driveLeft.getEncoder().getPosition();
    }

    @Override
    public double getRightEncoderRotations() {
        return driveRight.getEncoder().getPosition();
    }

    @Override
    public double getYaw() {
        return gyro.getYaw().getValue();
    }

    @Override
    public void resetGyro() {
        gyro.setYaw(0);
    }

    @Override
    public void resetEncoders() {
        driveLeft.getEncoder().setPosition(0);
        driveRight.getEncoder().setPosition(0);
    }

    @Override
    public void setArcadeSpeeds(double forward, double rotation) {
        diffDrive.arcadeDrive(forward, rotation);
    }
}//basically, this has all these hardware methods in it that were previously in Drivetrain, but got moved to make an IO interface. IO calls all the methods.