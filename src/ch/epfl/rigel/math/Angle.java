package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public final class Angle {

    public final static double TAU = 2 * Math.PI;
    private static final double RAD_PER_SEC = TAU / 1296000;
    private static final double RAD_PER_HR = TAU / 24;
    private static final double HR_PER_RAD = 24 / TAU;


    /**
     * normalizes an angle in the interval [0; TAU[
     */
    public static double normalizePositive(double rad){
        double result = rad;
        RightOpenInterval normaliseIntervalRad = RightOpenInterval.of(0,TAU);
        if (result<0){
            while (!normaliseIntervalRad.contains(result)){
                result += TAU;
            }
        } else {
            while (!normaliseIntervalRad.contains(result)) {
                result -= TAU;
            }
        }
        return result;
    }

    /**
     * converts from seconds to radians
     */
    public static double ofArcsec(double sec){
        return sec * RAD_PER_SEC;
    }

    /**
     * converts from degrees, minutes and seconds to radians
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkArgument(min >= 0 && min < 60 && sec >= 0 && sec < 60);
        double totalDeg = deg + (double) min/60 + sec/3600;
        return Math.toRadians(totalDeg);
    }

    /**
     * converts from degrees to radians
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**
     * converts from radians to degrees
     */
    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**
     * converts from hours to radians
     */
    public static double ofHr(double hr){
        return hr * RAD_PER_HR;
    }

    /**
     * converts from radians to hours
     */
    public static double toHr(double rad){
        return rad * HR_PER_RAD;
    }

}
