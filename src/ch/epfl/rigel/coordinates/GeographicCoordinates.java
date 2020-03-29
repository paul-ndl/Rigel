package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées géographiques
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class GeographicCoordinates extends SphericalCoordinates{

    /**
     * Construit des coordonnées géographiques
     * @param longitude
     *          la longitude
     * @param latitude
     *          la latitude
     */
    private GeographicCoordinates(double longitude, double latitude){
        super(longitude, latitude);
    }

    /**
     * Construit des coordonnées géographiques avec des arguments en degrés
     * @param lonDeg
     *          la longitude en degrés
     * @param latDeg
     *          la latitude en degrés
     * @throws IllegalArgumentException
     *          si longitude ou la latitude n'est pas valide
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkArgument(isValidLonDeg(lonDeg) && isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * Vérifie que la longitude est valide (appartient à l'intervalle [-180°, 180°[)
     * @param lonDeg
     * @return vrai si la longitude est valide
     */
    public static boolean isValidLonDeg(double lonDeg){
        RightOpenInterval longitudeIn = RightOpenInterval.symmetric(360);
        return longitudeIn.contains(lonDeg);
    }

    /**
     * Vérifie que la latitude est valide (appartient à l'intervalle [-90°, 90°])
     * @param latDeg
     * @return vrai si la latitude est valide
     */
    public static boolean isValidLatDeg(double latDeg){
        ClosedInterval latitudeIn = ClosedInterval.symmetric(180);
        return latitudeIn.contains(latDeg);
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
        return String.format(Locale.ROOT,"(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }

}
