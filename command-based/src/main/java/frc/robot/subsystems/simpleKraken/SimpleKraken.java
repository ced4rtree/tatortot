package frc.robot.subsystems.simpleKraken;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class SimpleKraken extends SubsystemBase {
    SimpleKrakenIOInputsAutoLogged inputs;
    SimpleKrakenIO io;

    public SimpleKraken() {
        inputs = new SimpleKrakenIOInputsAutoLogged();
        if (RobotBase.isReal()) {
            io = new SimpleKrakenIOReal();
        } else if (Robot.isReplay) {
            io = new SimpleKrakenIOSim();
        } else {
            io = new SimpleKrakenIO() {};
        }
    }

    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("SimpleKraken", inputs);
    }

    public Command runVoltage(DoubleSupplier voltageSupplier) {
        return this.run(
                () -> {
                    double voltage = voltageSupplier.getAsDouble();
                    io.setVoltage(voltage);
                });
    }
}
