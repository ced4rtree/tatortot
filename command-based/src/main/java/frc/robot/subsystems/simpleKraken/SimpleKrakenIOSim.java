package frc.robot.subsystems.simpleKraken;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class SimpleKrakenIOSim implements SimpleKrakenIO {
    private DCMotorSim motor;

    private double voltageApplied = 0.0;
    private double positionRadius = 0.0;
    private double temperatureF = 90.0;

    public SimpleKrakenIOSim() {
        motor =
                new DCMotorSim(
                        LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60(1), 0.1, 1),
                        DCMotor.getKrakenX60(1),
                        0.1,
                        0);
    }

    @Override
    public void setVoltage(double voltage) {
        voltageApplied = voltage;
    }

    public void updateInputs(SimpleKrakenIOInputs inputs) {
        // public void teleopPeriodic{
        motor.setInputVoltage(voltageApplied);

        double velocityRadPerSec = motor.getAngularVelocityRadPerSec();
        positionRadius += velocityRadPerSec * 0.02;

        temperatureF = voltageApplied * 1.1;

        inputs.motorVelocity = velocityRadPerSec;
        inputs.motorPosition = positionRadius;
        inputs.motorTemp = temperatureF;
    }
}
