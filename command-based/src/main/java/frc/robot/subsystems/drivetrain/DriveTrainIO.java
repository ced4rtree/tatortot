package frc.robot.subsystems.drivetrain;
public interface DriveTrainIO {
    // Get sensor values
    double getLeftEncoderRotations();
    double getRightEncoderRotations();
    double getYaw();

    // Reset sensors
    void resetGyro();
    void resetEncoders();

    // Drive motors
    void setArcadeSpeeds(double forward, double rotation);
    
    //Idk if there is supposed to be anything else in here. maybe the autologger?
    //so basically for suture reference, this calls all the methods in IO, and then Real has all the methods with the hardware in it.
}
