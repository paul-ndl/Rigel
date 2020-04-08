package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées horizontales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    private final static Interval AZIMUT_INTERVAL = RightOpenInterval.of(0,360);
    private final static Interval ALTITUDE_INTERVAL = ClosedInterval.symmetric(180);

    private final static Interval NORTH_INTERVAL_RIGHT = RightOpenInterval.of(0, 67.5);
    private final static Interval NORTH_INTERVAL_LEFT = RightOpenInterval.of(292.5, 360);
    private final static Interval SOUTH_INTERVAL = RightOpenInterval.of(112.5, 247.5);
    private final static Interval EAST_INTERVAL = RightOpenInterval.of(22.5, 157.5);
    private final static Interval WEST_INTERVAL = RightOpenInterval.of(202.5, 337.5);

    /**
     * Construit des coordonnées horizontales
     *
     * @param azimut   l'azimut
     * @param altitude l'altitude
     */
    private HorizontalCoordinates(double azimut, double altitude) {
        super(azimut, altitude);
    }

    /**
     * Construit des coordonnées horizontales avec des arguments en radians
     *
     * @param az  l'azimut en radians
     * @param alt l'altitude en radians
     * @throws IllegalArgumentException si l'azimut ou l'altitude n'est pas valide
     */
    public static HorizontalCoordinates of(double az, double alt) {
        Preconditions.checkInInterval(AZIMUT_INTERVAL, Angle.toDeg(az));
        Preconditions.checkInInterval(ALTITUDE_INTERVAL, Angle.toDeg(alt));
        return new HorizontalCoordinates(az, alt);
    }

    /**
     * Construit des coordonnées horizontales avec des arguments en degrés
     *
     * @param azDeg  l'azimut en degrés
     * @param altDeg l'altitude en degrés
     * @throws IllegalArgumentException si l'azimut ou l'altitude n'est pas valide
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Preconditions.checkInInterval(AZIMUT_INTERVAL, azDeg);
        Preconditions.checkInInterval(ALTITUDE_INTERVAL, altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * Retourne l'azimut en radians
     *
     * @see SphericalCoordinates#lon()
     */
    public double az() {
        return super.lon();
    }

    /**
     * Retourne l'azimut en degrés
     *
     * @see SphericalCoordinates#lonDeg()
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Retourne une représentation textuelle de l'octant dans lequel se trouve l'azimut
     *
     * @param n le caractère "nord"
     * @param e le caractère "est"
     * @param s le caractère "sud"
     * @param w le caractère "ouest"
     * @return une représentation textuelle de l'octant dans lequel se trouve l'azimut
     */
    public String azOctantName(String n, String e, String s, String w) {
        StringBuilder azOctant = new StringBuilder();
        if (NORTH_INTERVAL_RIGHT.contains(azDeg()) || NORTH_INTERVAL_LEFT.contains(azDeg())) {
            azOctant.append(n);
        }
        if (SOUTH_INTERVAL.contains(azDeg())) {
            azOctant.append(s);
        }
        if (EAST_INTERVAL.contains(azDeg())) {
            azOctant.append(e);
        }
        if (WEST_INTERVAL.contains(azDeg())) {
            azOctant.append(w);
        }
        return azOctant.toString();
    }

    /**
     * Retourne l'altitude en radians
     *
     * @see SphericalCoordinates#lat()
     */
    public double alt() {
        return super.lat();
    }

    /**
     * Retourne l'altitude en degrés
     *
     * @see SphericalCoordinates#latDeg()
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * Retourne la distance angulaire entre le récepteur et le point donné en argument
     *
     * @param that le point
     * @return la distance angulaire entre le récepteur et le point donné en argument
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double sinPhyThis = Math.sin(alt());
        double sinPhyThat = Math.sin(that.alt());
        double cosPhyThis = Math.cos(alt());
        double cosPhyThat = Math.cos(that.alt());
        double cosDelta = Math.cos(az() - that.az());
        double angularDistance = Math.acos(sinPhyThis*sinPhyThat  + cosPhyThis*cosPhyThat*cosDelta);
        return angularDistance;
    }

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     *
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                      "(az=%.4f°, alt=%.4f°)",
                             lonDeg(),
                             latDeg());
    }

}
