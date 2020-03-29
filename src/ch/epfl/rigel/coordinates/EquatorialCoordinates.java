package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées équatoriales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    /**
     * Construit des coordonnées équatoriales
     * @param rightAscension
     *          l'ascension droite
     * @param declination
     *          la déclinaison
     */
    private EquatorialCoordinates(double rightAscension, double declination){
        super(rightAscension, declination);
    }

    /**
     * Construit des coordonnées équatoriales avec des arguments en radians
     * @param ra
     *          l'ascension droite en radians
     * @param dec
     *          la déclinaison en radians
     */
    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkArgument(isValidRa(ra) && isValidDec(dec));
        return new EquatorialCoordinates(ra, dec);
    }

    /**
     * Vérifie que l'ascension droite est valide (appartient à l'intervalle [0, 2*PI[)
     * @param ra
     * @return vrai si l'ascension droite est valide
     */
    public static boolean isValidRa(double ra){
        final RightOpenInterval rightAscensionIn = RightOpenInterval.of(0, Angle.TAU);
        return rightAscensionIn.contains(ra);
    }

    /**
     * Vérifie que la déclinaison est valide (appartient à l'intervalle [-PI/4, PI/4])
     * @param dec
     * @return vrai si la déclinaison est valide
     */
    public static boolean isValidDec(double dec){
        final ClosedInterval declinationIn = ClosedInterval.symmetric(Angle.TAU/2);
        return declinationIn.contains(dec);
    }

    /**
     * Retourne l'ascension droite en radians
     * @see SphericalCoordinates#lon()
     */
    public double ra(){
        return super.lon();
    }

    /**
     * Retourne l'ascension droite en degrés
     * @see SphericalCoordinates#lonDeg()
     */
    public double raDeg(){
        return  super.lonDeg();
    }

    /**
     * Retourne l'ascension droite en heures
     * @return l'ascension droite en heures
     */
    public double raHr(){
        return  Angle.toHr(super.lon());
    }

    /**
     * Retourne la déclinaison en radians
     * @see SphericalCoordinates#lat()
     */
    public double dec(){
        return super.lat();
    }

    /**
     * Retourne la déclinaison en degrés
     * @see SphericalCoordinates#latDeg()
     */
    public double decDeg(){
        return super.latDeg();
    }

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }

}
