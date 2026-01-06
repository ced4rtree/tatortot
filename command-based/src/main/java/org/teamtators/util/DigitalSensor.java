package org.teamtators.util;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * An abstraction for either PNP or NPN digital sensors
 *
 * <p>Essentially allows for inverting the sensor reading
 */
public class DigitalSensor extends DigitalInput {
    private boolean inverted = false;

    public DigitalSensor(int channel, boolean inverted) {
        this(channel);
        this.inverted = inverted;
    }

    public DigitalSensor(int channel) {
        super(channel);
    }

    /**
     * @return the value from a digital input channel, taking into account the type
     */
    public boolean get() {
        boolean value = getRaw();
        if (inverted) {
            return !value;
        } else {
            return value;
        }
    }

    /**
     * @return The raw signal from the DigitalInput
     */
    public boolean getRaw() {
        return super.get();
    }

    public boolean getInverted() {
        return inverted;
    }
}
