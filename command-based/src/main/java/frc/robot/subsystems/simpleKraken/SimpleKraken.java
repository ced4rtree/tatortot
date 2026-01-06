package frc.robot.subsystems.simpleKraken;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;
import org.teamtators.tester.ManualTestGroup;
import org.teamtators.tester.components.MotorTest;
import org.teamtators.util.Subsystem;

public class SimpleKraken extends Subsystem {
    SimpleKrakenIOInputsAutoLogged inputs;
    SimpleKrakenIO io;

    public SimpleKraken() {
        inputs = new SimpleKrakenIOInputsAutoLogged();
        if (RobotBase.isReal()) {
            io = new SimpleKrakenIOReal();
        } else if (!Robot.isReplay) {
            io = new SimpleKrakenIOSim();
        } else {
            io = new SimpleKrakenIO() {};
        }
    }

    public void doPeriodic() {}

    @Override
    public void log() {
        io.updateInputs(inputs);
        Logger.processInputs("SimpleKraken", inputs);
    }

    @Override
    public boolean getHealth() {
        return true;
    }

    public Command runVoltage(DoubleSupplier voltageSupplier) {
        return this.run(
                () -> {
                    double voltage = voltageSupplier.getAsDouble();
                    io.setVoltage(voltage);
                });
    }

    @Override
    public ManualTestGroup createManualTests() {
        return new ManualTestGroup(
                "SimpleKraken", new MotorTest("Motuh", (input) -> io.setVoltage(input)));
    }
}
