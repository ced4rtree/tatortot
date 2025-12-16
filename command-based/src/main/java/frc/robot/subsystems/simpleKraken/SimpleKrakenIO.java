package frc.robot.subsystems.simpleKraken;

import org.littletonrobotics.junction.AutoLog;

public interface SimpleKrakenIO {
    @AutoLog
    public class SimpleKrakenIOInputs {
        public double motorPosition;
        public double motorVelocity;
        public double motorTemp;
    }

    public default void setVoltage(double voltage) {};

    public default void updateInputs(SimpleKrakenIOInputs inputs) {}
}
