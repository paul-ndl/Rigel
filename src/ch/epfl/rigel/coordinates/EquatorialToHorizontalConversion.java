package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;

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
        double azimut = Math.acos((Math.sin(dec) - latSin * Math.sin(ascRight)) / latCos * Math.cos(ascRight));
        double hauteur = Math.asin(Math.sin(dec) * latSin + Math.cos(dec * latCos * Math.cos(horairAngle)));
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
