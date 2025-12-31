package frc.robot.subsystems.simpleKraken;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class SimpleKrakenIOSim implements SimpleKrakenIO {
    private DCMotorSim motor;

    public SimpleKrakenIOSim() {
        motor =
                new DCMotorSim(
                        LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60(1), 0.1, 1),
                        DCMotor.getKrakenX60(1),
                        0,
                        0);
    }

    public void setVoltage(double volts) {
        motor.setInput(volts);
    }

    public void updateInputs(SimpleKrakenIOInputs inputs) {
        motor.update(.02);
        inputs.motorPosition = motor.getAngularPositionRotations();
        inputs.motorVelocity = motor.getAngularVelocityRPM();
        inputs.motorTemp = -1;
    }
}
