package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class SimpleKraken extends SubsystemBase {
    private TalonFX motor;
    private VoltageOut voltageRequest;

    public SimpleKraken() {
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

    public Command runVoltage(DoubleSupplier voltageSupplier) {
        return this.run(
                () -> {
                    // grab the current voltage being requested
                    double voltage = voltageSupplier.getAsDouble();

                    // store the voltage in a request object the motor can use
                    voltageRequest = voltageRequest.withOutput(voltage);

                    // tell the motor to use the voltage inside of the voltageRequest object
                    motor.setControl(voltageRequest);
                });
    }
}
