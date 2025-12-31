package frc.robot.subsystems.simpleKraken;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Temperature;

public class SimpleKrakenIOReal implements SimpleKrakenIO {
    private TalonFX motor;
    private VoltageOut voltageRequest;

    private StatusSignal<Angle> positionSignal;
    private StatusSignal<AngularVelocity> velocitySignal;
    private StatusSignal<Temperature> tempSignal;

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

        positionSignal = motor.getPosition();
        velocitySignal = motor.getVelocity();
        tempSignal = motor.getDeviceTemp();
    }

    public void setVoltage(double voltage) {
        // store the voltage in a request object the motor can use
        voltageRequest = voltageRequest.withOutput(voltage);

        // tell the motor to use the voltage inside of the voltageRequest object
        motor.setControl(voltageRequest);
    }

    public void updateInputs(SimpleKrakenIOInputs inputs) {
        BaseStatusSignal.refreshAll(positionSignal, velocitySignal, tempSignal);
        inputs.motorPosition = positionSignal.getValueAsDouble();
        inputs.motorVelocity = velocitySignal.getValueAsDouble();
        inputs.motorTemp = tempSignal.getValueAsDouble();
    }
}
