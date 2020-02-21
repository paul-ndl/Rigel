package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;


public final class Preconditions {
    private Preconditions() {}
    // … méthodes

    public static void checkArgument(boolean isTrue){
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}
