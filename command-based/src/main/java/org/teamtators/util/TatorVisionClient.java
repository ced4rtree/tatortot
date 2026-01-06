package org.teamtators.util;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Supplier;

public class TatorVisionClient {

    // 47.7164 degrees of rotation gave 480 pixels
    public static final double PIXELS_PER_RADIAN = 576.363140687;

    private abstract class EntryNames {
        static final String objectDetectionTable = "Object Target Detection";
        static final String objectCountKey = "Number Of Objects";
        static final String objectCentersKey = "Center";
        static final String objectWidthsKey = "Width";
        static final String stateMachineTable = "Vision State Machine";
        static final String visionStateKey = "Current State";
        static final String visionTable = "visionTable";
        static final String currentAngleKey = "currentAngle";
        static final String initialAngleKey = "initialAngle";
    }

    private final NetworkTableEntry objectCountEntry;
    private final NetworkTableEntry objectCentersEntry;
    private final NetworkTableEntry objectWidthsEntry;
    private final NetworkTableEntry visionStateEntry;
    private final NetworkTableEntry currentAngleEntry;
    private final NetworkTableEntry initialAngleEntry;

    private TatorVisionClient(Supplier<Rotation2d> yawSupplier) {
        var objDetectionTable =
                NetworkTableInstance.getDefault().getTable(EntryNames.objectDetectionTable);

        objectCountEntry = objDetectionTable.getEntry(EntryNames.objectCountKey);
        objectCentersEntry = objDetectionTable.getEntry(EntryNames.objectCentersKey);
        objectWidthsEntry = objDetectionTable.getEntry(EntryNames.objectWidthsKey);

        var stateMachineTable =
                NetworkTableInstance.getDefault().getTable(EntryNames.stateMachineTable);

        visionStateEntry = stateMachineTable.getEntry(EntryNames.visionStateKey);

        var visionTable = NetworkTableInstance.getDefault().getTable(EntryNames.visionTable);

        currentAngleEntry = visionTable.getEntry(EntryNames.currentAngleKey);
        initialAngleEntry = visionTable.getEntry(EntryNames.initialAngleKey);

        Commands.run(
                        () -> {
                            currentAngleEntry.setDouble(yawSupplier.get().getRotations());
                        })
                .ignoringDisable(true)
                .schedule();
    }

    public int getObjectCount() {
        return (int) objectCountEntry.getDouble(0.0);
    }

    private static double[] noDoubles = {};

    public double[] getObjectCenters() {
        var count = getObjectCount();
        if (count <= 0) {
            return noDoubles;
        } else {
            return objectCentersEntry.getDoubleArray(noDoubles);
        }
    }

    public double[] getObjectWidths() {
        var count = getObjectCount();
        if (count <= 0) {
            return noDoubles;
        } else {
            return objectWidthsEntry.getDoubleArray(noDoubles);
        }
    }

    public Rotation2d getInitialGyro() {
        return Rotation2d.fromRotations(initialAngleEntry.getDouble(0));
    }

    public enum TatorVisionStates {
        SHUFFLEBOARD,
        APRILTAG,
        BALL_DETECTION,
        TARGET_DETECTION,
        THRESHOLDING
    }

    public void setVisionMode(TatorVisionStates state) {
        if (state == null) {
            System.err.println("TatorVisionClient.setVisionMode called with a null state");
            return;
        }
        visionStateEntry.setString(state.toString());
    }
}
