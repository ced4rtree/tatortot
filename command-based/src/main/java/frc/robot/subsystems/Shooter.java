package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private SparkMax motor;

    public Shooter() {
        motor = new SparkMax(2, MotorType.kBrushless);

        SparkBaseConfig motorConfig =
                new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(25);

        motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command shoot() {
        return this.run(() -> motor.setVoltage(Volts.of(9)));
    }

    public Command stop() {
        return this.run(() -> motor.setVoltage(Volts.of(0)));
    }
}
