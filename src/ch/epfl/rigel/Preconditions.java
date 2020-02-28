package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;


public final class Preconditions {
    private Preconditions() {}

    /**
     * throws an exception if the argument is false
     */
    public static void checkArgument(boolean isTrue){
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * returns value if it is in the interval
     * throws an exception otherwise
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}
