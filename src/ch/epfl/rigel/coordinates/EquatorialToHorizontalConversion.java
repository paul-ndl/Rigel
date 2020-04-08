package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Une conversion de coordonnées équatoriales à horizontales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {


    private final double latCos, latSin;
    private final double sideralTime;

    /**
     * Construit une conversion entre les coordonnées équatoriales et horizontales
     * @param when
     *          le couple date/heure de la conversion
     * @param where
     *          le lieu de la conversion
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        latCos = Math.cos(where.lat());
        latSin = Math.sin(where.lat());
        sideralTime = SiderealTime.local(when, where);
    }

    /**
     * Retourne les coordonnées horizontales à partir des coordonnées équatoriales données
     * @param equ
     *          les coordonnées équatoriales
     * @return les coordonnées horizontales à partir des coordonnées équatoriales données
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ){
        double decCos = Math.cos(equ.dec());
        double decSin = Math.sin(equ.dec());
        double hCos = Math.cos(sideralTime - equ.ra());
        double hSin = Math.sin(sideralTime - equ.ra());
        double altitude = Math.asin(decSin*latSin + decCos*latCos*hCos);
        double azimut = Angle.normalizePositive(Math.atan2(-decCos*latCos*hSin, decSin - latSin*Math.sin(altitude)));
        return HorizontalCoordinates.of(azimut, altitude);
    }

    /**
     * Empêche d'utiliser cette méthode
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
