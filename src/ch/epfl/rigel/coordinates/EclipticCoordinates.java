package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées écliptiques
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    /**
     * Construit des coordonnées écliptiques
     * @param eclipticLatitude
     *          la longitude
     * @param eclipticLongitude
     *          la latitude
     */
    private EclipticCoordinates(double eclipticLongitude, double eclipticLatitude){
        super(eclipticLongitude, eclipticLatitude);
    }

    /**
     * Construit des coordonnées écliptiques avec des arguments en radians
     * @param lon
     *          la longitude en radians
     * @param lat
     *          la latitude en radians
     * @throws IllegalArgumentException
     *          si longitude ou la latitude n'est pas valide
     */
    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkArgument(isValidLon(lon) && isValidLat(lat));
        return new EclipticCoordinates(lon, lat);
    }

    /**
     * Vérifie que la longitude est valide (appartient à l'intervalle [0, 2*PI[)
     * @param lon
     * @return vrai si la longitude est valide
     */
    public static boolean isValidLon(double lon){
        final RightOpenInterval eclipticLongitudeIn = RightOpenInterval.of(0, Angle.TAU);
        return eclipticLongitudeIn.contains(lon);
    }

    /**
     * Vérifie que la latitude est valide (appartient à l'intervalle [-PI/4, PI/4])
     * @param lat
     * @return vrai si la latitude est valide
     */
    public static boolean isValidLat(double lat){
        final ClosedInterval eclipticLatitudeIn = ClosedInterval.symmetric(Angle.TAU/2);
        return eclipticLatitudeIn.contains(lat);
    }

    /**
     * @see SphericalCoordinates#lon()
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * @see SphericalCoordinates#lonDeg()
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * @see SphericalCoordinates#lat()
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * @see SphericalCoordinates#latDeg()
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }

}
