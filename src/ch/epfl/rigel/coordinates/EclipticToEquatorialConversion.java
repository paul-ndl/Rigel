package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.util.function.Function;

/**
 * Une conversion de coordonnées écliptiques à équatoriales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double obliquityCos;
    private final double obliquitySin;

    /**
     * Construit une conversion entre les coordonnées écliptiques et équatoriales
     * @param when
     *          le couple date/heure de la conversion
     */
    public EclipticToEquatorialConversion (ZonedDateTime when){
        final double centuries = Epoch.J2000.julianCenturiesUntil(when);
        final Polynomial p = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
        final double epsilon = p.at(centuries);
        obliquityCos = Math.cos(epsilon);
        obliquitySin = Math.sin(epsilon);
    }

    /**
     * Retourne les coordonnées équatoriales à partir des coordonnées écliptiques données
     * @param ecl
     *          les coordonnées écliptiques
     * @return les coordonnées équatoriales à partir des coordonnées écliptiques données
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        final double lonEcl = ecl.lon();
        final double latEcl = ecl.lat();
        final double ra = Angle.normalizePositive(Math.atan2(Math.sin(lonEcl) * obliquityCos - Math.tan(latEcl) * obliquitySin, Math.cos(lonEcl)));
        final double dec = Math.asin(Math.sin(latEcl) * obliquityCos + Math.cos(latEcl) * obliquitySin * Math.sin(lonEcl));
        return EquatorialCoordinates.of(ra, dec);
    }

    /**
     * Empêche d'utiliser cette méthode
     * @param o
     *          un objet
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * Empêche d'utiliser cette méthode
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
