package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Une conversion de coordonnées équatoriales à horizontales
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double sideralTime;
    private final double latCos;
    private final double latSin;

    /**
     * Construit une conversion entre les coordonnées équatoriales et horizontales
     * @param when
     *          le couple date/heure de la conversion
     * @param where
     *          le lieu de la conversion
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        final double latitude = where.lat();
        latCos = Math.cos(latitude);
        latSin = Math.sin(latitude);
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
        final double ra = equ.ra();
        final double dec = equ.dec();
        final double horairAngle = sideralTime - ra;
        final double altitude = Math.asin(Math.sin(dec) * latSin + Math.cos(dec) * latCos * Math.cos(horairAngle));
        final double azimut = Angle.normalizePositive(Math.atan2(-Math.cos(dec) * latCos * Math.sin(horairAngle), Math.sin(dec) - latSin * Math.sin(altitude)));
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
