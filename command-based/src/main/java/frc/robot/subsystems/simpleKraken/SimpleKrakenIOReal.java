package frc.robot.subsystems.simpleKraken;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class SimpleKrakenIOReal implements SimpleKrakenIO {
    private TalonFX motor;
    private VoltageOut voltageRequest;

    public SimpleKrakenIOReal() {
        motor = new TalonFX(1);
        voltageRequest = new VoltageOut(0);

        TalonFXConfiguration config =
                new TalonFXConfiguration()
                        .withCurrentLimits(
                                new CurrentLimitsConfigs()
                                        .withStatorCurrentLimit(10)
                                        .withSupplyCurrentLimit(10)
                                        .withSupplyCurrentLimitEnable(true)
                                        .withStatorCurrentLimitEnable(true));

        // try flashing the config up to 5 times
        // stops flashing after a success or 5 failures
        for (int i = 0; i < 5; i++) {
            if (motor.getConfigurator().apply(config).isOK()) {
                break;
            }
        }
    }

    public void setVoltage(double voltage) {
        // store the voltage in a request object the motor can use
        voltageRequest = voltageRequest.withOutput(voltage);

        // tell the motor to use the voltage inside of the voltageRequest object
        motor.setControl(voltageRequest);
    }
}
