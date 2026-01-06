package org.teamtators.util;

import java.util.*;

/**
 * A static class to convert enum classes to and from strings. Enum classes can be gotten with
 * {@code EnumType.class}, and stored in a {@code Class<?>}-type variable.
 */
public class EnumUtils {
    /**
     * Converts an Enum class to the names of its enums.
     *
     * @param <T> ignore this
     * @param enuum The enum class to convert. Get this with EnumType.class, and store it in a
     *     Class-type variable.
     * @return An ArrayList of strings.
     */
    public static <T extends Enum<T>> List<String> enum2nameslist(Class<?> enuum) {
        @SuppressWarnings("unchecked")
        Class<T> captured = (Class<T>) enuum;
        List<String> list = new ArrayList<String>();
        for (T item : captured.getEnumConstants()) {
            list.add(item.name());
        }
        return list;
    }

    /**
     * Get the enum an enum class has by name.
     *
     * @param <T> ignore this
     * @param enuum The enum class to convert.
     * @param name The name of the constant.
     * @return The constant of that name.
     * @exception java.lang.IllegalArgumentException If the enum has no such constant
     */
    public static <T extends Enum<T>> T name2const(Class<?> enuum, String name) {
        @SuppressWarnings("unchecked")
        Class<T> captured = (Class<T>) enuum;
        return Enum.valueOf(captured, name);
    }

    /**
     * Get the enum an enum class has by index.
     *
     * @param <T> ignore this
     * @param enuum The enum class to convert.
     * @param ind The index of the constant.
     * @return The constant of that name.
     * @exception java.lang.ArrayIndexOutOfBoundsException If the index is out of range.
     */
    public static <T extends Enum<T>> T num2const(Class<?> enuum, int ind) {
        @SuppressWarnings("unchecked")
        Class<T> captured = (Class<T>) enuum;
        return (T) captured.getEnumConstants()[ind];
    }
}
