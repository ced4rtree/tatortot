package org.teamtators.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class TatorMath {
    /**
     * Will calculate output of a piecewise graph based on the supplied matrix
     *
     * @param x the input of the piecewise function defined by the matrix called lookup
     * @param lookup the matrix of values where all values in f[a][0] are inputs and f[a][b] are
     *     outputs, where a and b are arbitrary values, and a >= 0 && b > 0
     * @param index the index of the desired output in lookup
     *     <p>e.g. lookup = {{1, 2, 0, 3}, {4, 5, 0, 6}, {7, 8, 0, 9}} linearlyInterpolate(2, lookup,
     *     3) will return 4.0
     * @return The desired output
     */
    public static double linearlyInterpolate(double x, double[][] lookup, int index) {
        return linearlyInterpolate(x, lookup, new int[] {index})[0];
    }

    /**
     * Will calculate outputs of a piecewise graph based on the supplied matrix
     *
     * @param x the input of the piecewise function defined by the matrix called lookup
     * @param lookup the matrix of values where all values in f[a][0] are inputs and f[a][b] are
     *     outputs, where a and b are arbitrary values, and a >= 0 && b > 0
     * @param indexes the indexes for which outputs you want to get out of lookup
     *     <p>e.g. lookup = {{1, 2, 0, 3}, {4, 5, 0, 6}, {7, 8, 0, 9}} linearlyInterpolate(2, lookup,
     *     new int[] {1, 3}) will return {3.0, 4.0}
     * @return The array of desired outputs. Will not include outputs from indexes that weren't
     *     specified in the indexes array
     */
    public static double[] linearlyInterpolate(double x, double[][] lookup, int[] indexes) {
        double[] ret = new double[indexes.length];

        for (int i = 0; i < indexes.length; i++) {
            int low = lookup.length - 1;
            for (int j = 1; j < lookup.length; j++) {
                if (lookup[j][0] > x) {

                    low = j - 1;
                    break;
                }
            }

            double y;
            int index = indexes[i];

            if (x < lookup[0][0]) {
                y = lookup[0][index];
            } else if (low >= lookup.length - 1) {
                y = lookup[low][index];
            } else {
                double y2 = lookup[low + 1][index];
                double y1 = lookup[low][index];
                double x2 = lookup[low + 1][0];
                double x1 = lookup[low][0];

                double slope = (y2 - y1) / (x2 - x1);

                y = slope * (x - x1) + y1;
            }

            ret[i] = y;
        }

        return ret;
    }

    /**
     * A negative-safe modulus operation
     *
     * @return a % b, but safe with negative numbers
     * @param a any arbitrary number
     * @param b any arbitrary number that's not 0
     */
    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }

    /**
     * Wrap a Rotation2d to -pi <-> pi.
     *
     * @return wrapped
     * @param a angle
     */
    public static Rotation2d wrapRotation2d(Rotation2d a) {
        return a.plus(Rotation2d.kZero); // .plus() wraps so this works
    }

    public static Pose2d rotateInPlace(Pose2d pose, Rotation2d by) {
        return pose.rotateAround(pose.getTranslation(), by);
    }
}
