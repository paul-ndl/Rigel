package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;

import java.time.*;
import java.util.function.Function;


public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private double obliquityCos;
    private double obliquitySin;

    public EclipticToEquatorialConversion (ZonedDateTime when){
        double centuries = Epoch.J2000.julianCenturiesUntil(when);
        double epsilon = Angle.ofArcsec(0.00181) * centuries * centuries * centuries - Angle.ofArcsec(0.0006) * centuries * centuries - Angle.ofArcsec(46.815) * centuries + Angle.ofDMS(23, 26, 21.45);
        obliquityCos = Math.cos(epsilon);
        obliquitySin = Math.sin(epsilon);
    }

    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        double lonEcl = ecl.lon();
        double latEcl = ecl.lat();
        double ascRight = Math.atan2(Math.sin(lonEcl) * obliquityCos - Math.tan(latEcl) * obliquitySin, Math.cos(lonEcl));
        double dec = Math.asin(Math.sin(latEcl) * obliquityCos + Math.cos(latEcl) * obliquitySin * Math.sin(lonEcl));
        return  EquatorialCoordinates.of(ascRight, dec);
    }

    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

    public static void main (String args[]){
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2009, Month.JULY, 6), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        System.out.println(e.apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10), Angle.ofDMS(4,52,31))).toString());
        System.out.println("ra=" + (9 + (double) 34/60 + (double) 53/3600 + (double) 32/360000));
        System.out.println("dec=" + Angle.toDeg(Angle.ofDMS(19,32,6)));
    }

}
