package org.teamtators.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public final class QuickDebug {
    static NetworkTable quickTable = null;

    private static void ensureTable() {
        if (quickTable == null) {
            quickTable = NetworkTableInstance.getDefault().getTable("quick");
        }
    }

    public static double input(String name, double defaultValue) {
        ensureTable();
        if (!quickTable.containsKey(name)) {
            quickTable.getEntry(name).setDouble(defaultValue);
            return defaultValue;
        }
        return quickTable.getEntry(name).getDouble(defaultValue);
    }

    public static boolean input(String name, boolean defaultValue) {
        ensureTable();
        if (!quickTable.containsKey(name)) {
            quickTable.getEntry(name).setBoolean(defaultValue);
            return defaultValue;
        }
        return quickTable.getEntry(name).getBoolean(defaultValue);
    }

    public static <T extends Enum<T>> T input(String name, /*Class<T> enumType, */ T defaultValue) {
        var enumType = defaultValue.getClass();
        ensureTable();
        var subtable = quickTable.getSubTable(name);
        if (!subtable.containsKey(".enumname")
                || !subtable.getEntry(".enumname").getString("").equals(enumType.getName())) {
            subtable.getEntry(".controllable").setBoolean(true);
            subtable.getEntry(".instance").setInteger(0);
            subtable.getEntry(".name").setString(name);
            subtable.getEntry(".type").setString("String Chooser");
            subtable.getEntry(".enumname").setString(enumType.getName());
            subtable.getEntry("active").setString(defaultValue.name());
            subtable.getEntry("default").setString(defaultValue.name());
            subtable
                    .getEntry("options")
                    .setStringArray(EnumUtils.enum2nameslist(enumType).toArray(new String[] {}));
            return defaultValue;
        }
        if (subtable.containsKey("selected")) {
            var selectedName = subtable.getEntry("selected").getString(defaultValue.name());
            try {
                T selectedEnum = EnumUtils.name2const(enumType, selectedName);
                subtable.getEntry("active").setString(selectedName);
                return selectedEnum;
            } catch (java.lang.IllegalArgumentException e) {
                subtable.getEntry("selected").setString(defaultValue.name());
                subtable.getEntry("active").setString(defaultValue.name());
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static void output(String name, double value) {
        ensureTable();
        quickTable.getEntry(name).setDouble(value);
    }

    public static void output(String name, String value) {
        ensureTable();
        quickTable.getEntry(name).setString(value);
    }

    public static void output(String name, boolean value) {
        ensureTable();
        quickTable.getEntry(name).setBoolean(value);
    }
}
