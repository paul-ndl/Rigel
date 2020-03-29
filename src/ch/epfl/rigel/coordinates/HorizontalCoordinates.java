package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées horizontales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * Construit des coordonnées horizontales
     * @param azimut
     *          l'azimut
     * @param altitude
     *          l'altitude
     */
    private HorizontalCoordinates(double azimut, double altitude){
        super(azimut, altitude);
    }

    /**
     * Construit des coordonnées horizontales avec des arguments en radians
     * @param az
     *          l'azimut en radians
     * @param alt
     *          l'altitude en radians
     * @throws IllegalArgumentException
     *          si l'azimut ou l'altitude n'est pas valide
     */
    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkArgument(isValidAzDeg(Angle.toDeg(az)) && isValidAltDeg(Angle.toDeg(alt)));
        return new HorizontalCoordinates(az, alt);
    }

    /**
     * Construit des coordonnées horizontales avec des arguments en degrés
     * @param azDeg
     *          l'azimut en degrés
     * @param altDeg
     *          l'altitude en degrés
     * @throws IllegalArgumentException
     *          si l'azimut ou l'altitude n'est pas valide
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        Preconditions.checkArgument(isValidAzDeg(azDeg) && isValidAltDeg(altDeg));
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * Vérifie que l'azimut est valide (appartient à l'intervalle [0°, 360°[)
     * @param azDeg
     * @return vrai si l'azimut est valide
     */
    public static boolean isValidAzDeg(double azDeg){
        RightOpenInterval azimutIn = RightOpenInterval.of(0, 360);
        return azimutIn.contains(azDeg);
    }

    /**
     * Vérifie que l'altitude est valide (appartient à l'intervalle [-90°, 90°])
     * @param altDeg
     * @return vrai si l'altitude est valide
     */
    public static boolean isValidAltDeg(double altDeg){
        ClosedInterval altitudeIn = ClosedInterval.symmetric(180);
        return altitudeIn.contains(altDeg);
    }

    /**
     * Retourne l'azimut en radians
     * @see SphericalCoordinates#lon()
     */
    public double az(){
        return super.lon();
    }

    /**
     * Retourne l'azimut en degrés
     * @see SphericalCoordinates#lonDeg()
     */
    public double azDeg(){
        return  super.lonDeg();
    }

    /**
     * Retourne une représentation textuelle de l'octant dans lequel se trouve l'azimut
     * @param n
     *          le caractère "nord"
     * @param e
     *          le caractère "est"
     * @param s
     *          le caractère "sud"
     * @param w
     *          le caractère "ouest"
     * @return une représentation textuelle de l'octant dans lequel se trouve l'azimut
     */
    public String azOctantName(String n, String e, String s, String w) {
        final StringBuilder azOctant = new StringBuilder();
        if (RightOpenInterval.of(0, 67.5).contains(azDeg()) || RightOpenInterval.of(292.5, 360).contains(azDeg())) {
            azOctant.append(n);
        }
        if (RightOpenInterval.of(112.5, 247.5).contains(azDeg())) {
            azOctant.append(s);
        }
        if (RightOpenInterval.of(22.5, 157.5).contains(azDeg())) {
            azOctant.append(e);
        }
        if (RightOpenInterval.of(202.5, 337.5).contains(azDeg())) {
            azOctant.append(w);
        }
        return azOctant.toString();
    }

    /**
     * Retourne l'altitude en radians
     * @see SphericalCoordinates#lat()
     */
    public double alt(){
        return super.lat();
    }

    /**
     * Retourne l'altitude en degrés
     * @see SphericalCoordinates#latDeg()
     */
    public double altDeg(){
        return super.latDeg();
    }

    /**
     * Retourne la distance angulaire entre le récepteur et le point donné en argument
     * @param that
     *          le point
     * @return la distance angulaire entre le récepteur et le point donné en argument
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        double angularDistance;
        angularDistance = Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt()) * Math.cos(that.alt()) * (Math.cos(this.az() - that.az())));
        return angularDistance;
    }

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(az=%.4f°, alt=%.4f°)", lonDeg(), latDeg());
    }

}
