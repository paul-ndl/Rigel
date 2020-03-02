package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double sideralTime;
    private double latCos;
    private double latSin;

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        double latitude = where.lat();
        latCos = Math.cos(latitude);
        latSin = Math.sin(latitude);
        sideralTime = SiderealTime.local(when, where);
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ){
        double ascRight = equ.ra();
        double dec = equ.dec();
        double horairAngle = sideralTime - ascRight;
        double hauteur = Math.asin(Math.sin(dec) * latSin + Math.cos(dec) * latCos * Math.cos(horairAngle));
        double azimut = Angle.normalizePositive(Math.atan2(-Math.cos(dec) * latCos * Math.sin(horairAngle), Math.sin(dec) - latSin * Math.sin(hauteur)));
        return HorizontalCoordinates.of(azimut, hauteur);
    }

    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }
}
