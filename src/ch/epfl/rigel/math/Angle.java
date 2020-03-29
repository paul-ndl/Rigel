package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Un Angle
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Angle {

    public final static double TAU = 2 * Math.PI;
    private final static double RAD_PER_SEC = TAU / 1296000;
    private final static double RAD_PER_HR = TAU / 24;
    private final static double HR_PER_RAD = 24 / TAU;


    /**
     * Normalise l'angle en le réduisant à l'intervalle [O,TAU[
     * @param rad
     *          l'angle
     * @return l'angle normalisé
     */
    public static double normalizePositive(double rad){
       double result = rad%TAU;
       result += TAU;
       return result%TAU;
    }

    /**
     * Convertit l'angle de secondes d'arc à radians
     * @param sec
     *          l'angle en secondes d'arc
     * @return l'angle convertit
     */
    public static double ofArcsec(double sec){
        return (sec * RAD_PER_SEC);
    }

    /**
     * Convertit l'angle de degrés, minutes et secondes à radians
     * @param deg
     *          les degrés de l'angle
     * @param min
     *          les minutes de l'angle
     * @param sec
     *          les secondes de l'angle
     * @throws IllegalArgumentException
     *          si les minutes ou secondes ne sont pas comprises entre 0 et 60
     * @return l'angle convertit
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkInInterval(RightOpenInterval.of(0,60), min);
        Preconditions.checkInInterval(RightOpenInterval.of(0,60), sec);
        return Math.toRadians(deg + (double) min/60 + sec/3600);
    }

    /**
     * Convertit l'angle de degrés à radians
     * @param deg
     *          l'angle en degrés
     * @return l'angle convertit
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**
     * Convertit l'angle de radians à degrés
     * @param rad
     *          l'angle en radians
     * @return l'angle convertit
     */
    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**
     * Convertit l'angle de heures à radians
     * @param hr
     *          l'angle en heures
     * @return l'angle convertit
     */
    public static double ofHr(double hr){
        return (hr * RAD_PER_HR);
    }

    /**
     * Convertit l'angle de radians à heures
     * @param rad
     *          l'angle en radians
     * @return l'angle convertit
     */
    public static double toHr(double rad){
        return (rad * HR_PER_RAD);
    }

}
