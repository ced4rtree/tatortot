// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.simpleKraken.SimpleKraken;
import java.io.File;
import java.util.ArrayList;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.teamtators.tester.ManualTestGroup;
import org.teamtators.tester.ManualTester;
import org.teamtators.util.DeviceHealthManager;
import org.teamtators.util.RobotStateListener;
import org.teamtators.util.Subsystem;
import org.teamtators.util.XBOXController;

public class Robot extends LoggedRobot {
    // testing library stuff
    public enum RobotControlMode {
        Teleop,
        Autonomous,
        Disabled,
        Test
    }

    private RobotControlMode currentControlMode = RobotControlMode.Disabled;
    private RobotControlMode newControlMode = RobotControlMode.Disabled;
    private ArrayList<RobotStateListener> stateListeners;
    private ManualTester manualTester;

    public static final boolean isReplay = false;

    private Command autonomousCommand;

    private CommandXboxController controller;

    private Drivetrain drivetrain;
    private Intake intake;
    private Shooter shooter;
    private SimpleKraken simpleKraken;

    private static Robot instance;

    public Robot() {
        instance = this;

        configureAdvantageKit();

        controller = new CommandXboxController(0);

        stateListeners = new ArrayList<>();

        drivetrain = new Drivetrain();
        intake = new Intake();
        shooter = new Shooter();
        simpleKraken = new SimpleKraken();

        manualTester = new ManualTester();

        // have the kraken follow the value of the right trigger by default
        simpleKraken.setDefaultCommand(simpleKraken.runVoltage(controller::getLeftY));

        // if no other command is running on the drivetrain, make it drive
        // drivetrain.setDefaultCommand(
        //         drivetrain.drive(controller::getLeftY, () -> -controller.getRightX()));

        // // when the left trigger is held, and the sensor is NOT hit, run the picker motor
        // // `whileTrue` kills that command if either condition becomes false
        // controller.leftTrigger().and(intake.gamePieceDetected.negate()).whileTrue(intake.feed());

        // // when the right trigger is held, and the sensor is hit, shoot the game piece
        // controller.rightBumper().and(intake.gamePieceDetected).onTrue(shooter.shoot());

        // // once the gamepiece has left the sensor for 0.5 seconds, stop shooting
        // intake.gamePieceDetected.negate().debounce(0.5).onTrue(shooter.stop());
    }

    private void configureAdvantageKit() {
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSha", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);

        switch (BuildConstants.DIRTY) {
            case 0:
                Logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                Logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                Logger.recordMetadata("GitDirty", "Unknown");
                break;
        }

        if (RobotBase.isReal() || !isReplay) {
            File logDir = new File(RobotBase.isReal() ? "/home/lvuser/logs" : "logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            Logger.addDataReceiver(new WPILOGWriter(logDir.toString()));
            Logger.addDataReceiver(new NT4Publisher());
        } else { // replaying logged data
            setUseTiming(false); // disable 20ms loop, process the replay as fast as possible

            String logPath = LogFileUtil.findReplayLog();
            Logger.setReplaySource(new WPILOGReader(logPath));
            Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_replay")));
        }

        Logger.start();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().enable();
        if (currentControlMode != RobotControlMode.Disabled) {
            DeviceHealthManager.printHealth(currentControlMode);
        }
        updateStateListeners(RobotControlMode.Disabled);
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        updateStateListeners(RobotControlMode.Autonomous);

        // autonomousCommand =
        //         drivetrain
        //                 .drive(() -> 0.5, () -> 0) // drive at half power straight forward
        //                 .until(
        //                         () ->
        //                                 drivetrain.getOdometryPose().getTranslation().getX()
        //                                         >= 1) // until you've passed 1 meter
        //                 .andThen(drivetrain.turn(180.0)) // after that, turn 180 degrees
        //                 .andThen(
        //                         drivetrain
        //                                 .drive(
        //                                         () -> 0.5, () -> 0.0) // and after that, drive half
        // power straight forward
        //                                 // ("forward" is robot relative in this case, meaning drive
        // towards the bots
        //                                 // front, which is now turned 180 degrees)
        //                                 .until(() ->
        // drivetrain.getOdometryPose().getTranslation().getX() <= 0))
        //                 // ^-- stop driving once you've reached the starting position of 0 meters
        //                 .beforeStarting(drivetrain::resetGyro); // and be sure to reset the gyro
        // before driving

        // if (autonomousCommand != null) {
        //     autonomousCommand.schedule();
        // }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        updateStateListeners(RobotControlMode.Teleop);
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        var commandScheduler = CommandScheduler.getInstance();
        commandScheduler.cancelAll();
        commandScheduler.disable();
        updateStateListeners(RobotControlMode.Test);
        configureTests();
        manualTester.initialize();
    }

    @Override
    public void testPeriodic() {
        manualTester.execute();
    }

    @Override
    public void testExit() {}

    // stuff needed for our testing software
    public void registerStateListener(RobotStateListener stateListener) {
        stateListeners.add(stateListener);
    }

    public void unregisterStateListener(RobotStateListener stateListener) {
        stateListeners.remove(stateListener);
    }

    public void updateStateListeners(RobotControlMode controlMode) {
        newControlMode = controlMode;
        if (currentControlMode != newControlMode) {
            if (currentControlMode == RobotControlMode.Test) {
                manualTester.end(true);
            }
            currentControlMode = newControlMode;
        }
        for (int i = 0; i < stateListeners.size(); i++) {
            stateListeners.get(i).onEnterRobotState(controlMode);
        }
    }

    protected void configureTests() {
        System.out.println("Configuring tests");
        manualTester.clearTestGroups();
        manualTester.setController(new XBOXController(0));

        for (Subsystem subsystem : Subsystem.getSubsystemList()) {
            ManualTestGroup group = subsystem.createManualTests();
            if (group != null) {
                manualTester.registerTestGroup(group);
                System.out.println(subsystem.getName());
            }
        }
    }

    public static Robot getInstance() {
        return instance;
    }
}
