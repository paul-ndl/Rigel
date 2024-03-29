package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Une conversion de coordonnées écliptiques à équatoriales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double obliquityCos, obliquitySin;

    private final static Polynomial polynomial = Polynomial.of(Angle.ofArcsec(0.00181),
                                                               -Angle.ofArcsec(0.0006),
                                                               -Angle.ofArcsec(46.815),
                                                               Angle.ofDMS(23, 26, 21.45));

    /**
     * Construit une conversion entre les coordonnées écliptiques et équatoriales
     *
     * @param when le couple date/heure de la conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double centuries = Epoch.J2000.julianCenturiesUntil(when);
        double obliquity = polynomial.at(centuries);
        obliquityCos = Math.cos(obliquity);
        obliquitySin = Math.sin(obliquity);
    }

    /**
     * Retourne les coordonnées équatoriales à partir des coordonnées écliptiques données
     *
     * @param ecl les coordonnées écliptiques
     * @return les coordonnées équatoriales à partir des coordonnées écliptiques données
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double lonCos = Math.cos(ecl.lon());
        double lonSin = Math.sin(ecl.lon());
        double latCos = Math.cos(ecl.lat());
        double latSin = Math.sin(ecl.lat());
        double latTan = Math.tan(ecl.lat());
        double ra = Angle.normalizePositive(Math.atan2(lonSin*obliquityCos - latTan*obliquitySin, lonCos));
        double dec = Math.asin(latSin*obliquityCos + latCos*obliquitySin*lonSin);
        return EquatorialCoordinates.of(ra, dec);
    }

    /**
     * Empêche d'utiliser cette méthode
     *
     * @param o un objet
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Empêche d'utiliser cette méthode
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}
