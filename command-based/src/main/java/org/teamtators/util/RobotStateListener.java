package org.teamtators.util;

import frc.robot.Robot.RobotControlMode;

public interface RobotStateListener {
    void onEnterRobotState(RobotControlMode state);
}
