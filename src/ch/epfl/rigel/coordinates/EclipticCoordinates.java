package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées écliptiques
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private final static Interval LONGITUDE_INTERVAL = RightOpenInterval.of(0,Angle.TAU);
    private final static Interval LATITUDE_INTERVAL = ClosedInterval.symmetric(Angle.TAU/2);

    /**
     * Construit des coordonnées écliptiques
     *
     * @param eclipticLatitude  la longitude
     * @param eclipticLongitude la latitude
     */
    private EclipticCoordinates(double eclipticLongitude, double eclipticLatitude) {
        super(eclipticLongitude, eclipticLatitude);
    }

    /**
     * Construit des coordonnées écliptiques avec des arguments en radians
     *
     * @param lon la longitude en radians
     * @param lat la latitude en radians
     * @throws IllegalArgumentException si longitude ou la latitude n'est pas valide
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Preconditions.checkInInterval(LONGITUDE_INTERVAL, lon);
        Preconditions.checkInInterval(LATITUDE_INTERVAL, lat);
        return new EclipticCoordinates(lon, lat);
    }

    /**
     * @see SphericalCoordinates#lon()
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * @see SphericalCoordinates#lonDeg()
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * @see SphericalCoordinates#lat()
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * @see SphericalCoordinates#latDeg()
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     *
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                      "(λ=%.4f°, β=%.4f°)",
                             lonDeg(),
                             latDeg());
    }

}
