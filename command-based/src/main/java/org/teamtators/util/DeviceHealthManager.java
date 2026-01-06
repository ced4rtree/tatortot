package org.teamtators.util;

import frc.robot.Robot.RobotControlMode;
import org.littletonrobotics.junction.Logger;

/** A utility class to keep track of subsystems going down. */
public abstract class DeviceHealthManager {

    private static String currentFaults = "";
    private static String stickyFaults = "";
    private static boolean currentlyHealthy = true;
    private static boolean stickyHealthy = true;

    public static void printHealth(RobotControlMode mode) {
        System.out.println();
        System.out.println("===== ROBOT HEALTH REPORT =====");
        if (mode != null) {
            System.out.println(">>>>>>> From " + mode.name());
        }
        System.out.println();
        if (currentlyHealthy) {
            System.out.println("Current faults: none");
        } else {
            // -2 for the extra ", " at the end
            System.out.println(
                    "Current faults: " + currentFaults.substring(0, currentFaults.length() - 2));
        }
        if (stickyHealthy) {
            System.out.println("Sticky faults: none");
        } else {
            // -2 for the extra ", " at the end
            System.out.println("Sticky faults: " + stickyFaults.substring(0, stickyFaults.length() - 2));
        }
        System.out.println();
        System.out.println("===============================");
        System.out.println();
    }

    public static void logHealth() {
        currentlyHealthy = true;
        stickyHealthy = true;
        currentFaults = "";
        stickyFaults = "";
        for (Subsystem subsystem : Subsystem.getSubsystemList()) {
            if (!subsystem.processHealth()) {
                currentlyHealthy = false;
                currentFaults += subsystem.getName() + ", ";
            }
            if (!subsystem.getStickyHealth()) {
                stickyHealthy = false;
                stickyFaults += subsystem.getName() + ", ";
            }
        }
        // momentary
        Logger.recordOutput("Health/Momentary/Healthy", currentlyHealthy);
        if (!currentlyHealthy) {
            Logger.recordOutput(
                    "Health/Momentary/Faults", currentFaults.substring(0, currentFaults.length() - 2));
        } else {
            Logger.recordOutput("Health/Momentary/Faults", "nothing");
        }
        // sticky
        Logger.recordOutput("Health/Sticky/Healthy", stickyHealthy);
        if (!stickyHealthy) {
            Logger.recordOutput(
                    "Health/Sticky/Faults", stickyFaults.substring(0, stickyFaults.length() - 2));
        } else {
            Logger.recordOutput("Health/Sticky/Faults", "nothing");
        }
    }

    public static boolean isCurrentlyHealthy() {
        return currentlyHealthy;
    }

    public static boolean isStickyHealthy() {
        return stickyHealthy;
    }
}
