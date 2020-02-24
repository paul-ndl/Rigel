package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

public final class Angle {

    public final static double TAU = 2 * Math.PI;
    private static final double RAD_PER_SEC = TAU / 1296000;
    private static final double RAD_PER_HR = TAU / 24;
    private static final double HR_PER_RAD = 24 / TAU;

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

    public static double ofArcsec(double sec){
        return sec * RAD_PER_SEC;
    }

    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkArgument(min >= 0 && min < 60 && sec >= 0 && sec < 60);
        double totalDeg = deg + (double) min/60 + sec/3600;
        return Math.toRadians(totalDeg);
    }

    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    public static double ofHr(double hr){
        return hr * RAD_PER_HR;
    }

    public static double toHr(double rad){
        return rad * HR_PER_RAD;
    }

}
