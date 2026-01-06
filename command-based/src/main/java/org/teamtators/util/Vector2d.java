package org.teamtators.util;

public class Vector2d {
    private double x;
    private double y;

    public Vector2d() {
        this(0, 0);
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2d fromPolar(double theta, double magnitude) {
        double x = magnitude * Math.cos(theta);
        double y = magnitude * Math.sin(theta);
        return new Vector2d(x, y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
