package org.teamtators.util;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.Robot.RobotControlMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import org.teamtators.tester.ManualTestGroup;
import org.teamtators.tester.ManualTestable;

public abstract class Subsystem extends SubsystemBase
        implements ManualTestable, RobotStateListener {

    private final EnumSet<RobotControlMode> validStates;
    protected RobotControlMode robotState = RobotControlMode.Disabled;
    private boolean testSubsystem = false;
    private static ArrayList<Subsystem> subsystems = new ArrayList<>();

    public Subsystem() {
        setName(getClass().getSimpleName());
        validStates = EnumSet.of(RobotControlMode.Teleop, RobotControlMode.Autonomous);
        Robot.getInstance().registerStateListener(this);
        System.out.println(getClass().getSimpleName() + " Created");
        subsystems.add(this);
    }

    @Override
    public void onEnterRobotState(RobotControlMode state) {
        setCurrentState(state);
        printName();
    }

    public final void testPeriodic() {
        doPeriodic();
    }

    @Override
    public final void periodic() {
        if (validIn(robotState)) {
            doPeriodic();
        } else if (robotState == RobotControlMode.Test) {
            if (testSubsystem == true) {
                doPeriodic();
            }
        }
    }

    public void configure() {}

    public void setTestSubsystem(boolean bool) {
        testSubsystem = bool;
    }

    public void doPeriodic() {}

    public String printName() {
        return ("unOverrided subsystem name");
    }

    @Override
    public ManualTestGroup createManualTests() {
        ManualTestGroup tests = new ManualTestGroup(getName());
        return tests;
    }

    public final void setValidity(RobotControlMode... validStates) {
        this.validStates.clear();
        this.validStates.addAll(Arrays.asList(validStates));
    }

    public final boolean validIn(RobotControlMode robotState) {
        return validStates.contains(robotState);
    }

    public final void setCurrentState(RobotControlMode robotState) {
        this.robotState = robotState;
    }

    public RobotControlMode getRobotState() {
        return robotState;
    }

    public static ArrayList<Subsystem> getSubsystemList() {
        return subsystems;
    }

    public abstract void log();

    public final Optional<Command> getPossibleCommand() {
        if (this.getCurrentCommand() != null) {
            return Optional.of(this.getCurrentCommand());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Should report whether a subsystem is healthy. See {@link DeviceHealthManager}
     *
     * @return false if there's something wrong, true if all is good
     */
    public abstract boolean getHealth();

    /**
     * Checks if the subsystem is healthy, and also flip {@link #stickyHealth} to false if down.
     *
     * @return if the subsystem is currently healthy
     */
    protected final boolean processHealth() {
        boolean healthy = getHealth();
        if (!healthy) {
            stickyHealth = false;
        }
        return healthy;
    }

    private boolean stickyHealth = true;

    /**
     * @return whether the subsystem has ever been unhealthy
     */
    public boolean getStickyHealth() {
        return stickyHealth;
    }
}
