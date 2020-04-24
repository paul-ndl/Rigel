package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées équatoriales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private final static Interval RIGHT_ASCENSION_INTERVAL = RightOpenInterval.of(0, Angle.TAU);
    private final static Interval DECLINATION_INTERVAL = ClosedInterval.symmetric(Angle.TAU/2);

    /**
     * Construit des coordonnées équatoriales
     *
     * @param rightAscension l'ascension droite
     * @param declination    la déclinaison
     */
    private EquatorialCoordinates(double rightAscension, double declination) {
        super(rightAscension, declination);
    }

    /**
     * Construit des coordonnées équatoriales avec des arguments en radians
     *
     * @param ra  l'ascension droite en radians
     * @param dec la déclinaison en radians
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        Preconditions.checkInInterval(RIGHT_ASCENSION_INTERVAL, ra);
        Preconditions.checkInInterval(DECLINATION_INTERVAL, dec);
        return new EquatorialCoordinates(ra, dec);
    }

    /**
     * Retourne l'ascension droite en radians
     *
     * @see SphericalCoordinates#lon()
     */
    public double ra() {
        return super.lon();
    }

    /**
     * Retourne l'ascension droite en degrés
     *
     * @see SphericalCoordinates#lonDeg()
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * Retourne l'ascension droite en heures
     *
     * @return l'ascension droite en heures
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**
     * Retourne la déclinaison en radians
     *
     * @see SphericalCoordinates#lat()
     */
    public double dec() {
        return super.lat();
    }

    /**
     * Retourne la déclinaison en degrés
     *
     * @see SphericalCoordinates#latDeg()
     */
    public double decDeg() {
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
                      "(ra=%.4fh, dec=%.4f°)",
                             raHr(),
                             decDeg());
    }

}
