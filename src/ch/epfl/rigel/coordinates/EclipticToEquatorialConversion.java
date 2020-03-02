package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.util.function.Function;


public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private double obliquityCos;
    private double obliquitySin;

    public EclipticToEquatorialConversion (ZonedDateTime when){
        double centuries = Epoch.J2000.julianCenturiesUntil(when);
        Polynomial p = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
        double epsilon = p.at(centuries);
        obliquityCos = Math.cos(epsilon);
        obliquitySin = Math.sin(epsilon);
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        double lonEcl = ecl.lon();
        double latEcl = ecl.lat();
        double ascRight = Angle.normalizePositive(Math.atan2(Math.sin(lonEcl) * obliquityCos - Math.tan(latEcl) * obliquitySin, Math.cos(lonEcl)));
        double dec = Math.asin(Math.sin(latEcl) * obliquityCos + Math.cos(latEcl) * obliquitySin * Math.sin(lonEcl));
        return EquatorialCoordinates.of(ascRight, dec);
    }

    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    @Override
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
