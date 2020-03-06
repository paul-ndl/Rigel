package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.util.function.Function;


public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private double obliquityCos;
    private double obliquitySin;

    /**
     * constructs a change between ecliptic and equatorial coordinates
     * calculate the ecliptic obliquity
     */
    public EclipticToEquatorialConversion (ZonedDateTime when){
        double centuries = Epoch.J2000.julianCenturiesUntil(when);
        Polynomial p = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
        double epsilon = p.at(centuries);
        obliquityCos = Math.cos(epsilon);
        obliquitySin = Math.sin(epsilon);
    }

    /**
     * returns equatorial coordinates that correspond to the given ecliptic coordinates
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        double lonEcl = ecl.lon();
        double latEcl = ecl.lat();
        double ascRight = Angle.normalizePositive(Math.atan2(Math.sin(lonEcl) * obliquityCos - Math.tan(latEcl) * obliquitySin, Math.cos(lonEcl)));
        double dec = Math.asin(Math.sin(latEcl) * obliquityCos + Math.cos(latEcl) * obliquitySin * Math.sin(lonEcl));
        return EquatorialCoordinates.of(ascRight, dec);
    }

    /**
     * prevents to use this method
     */
    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * prevents to use this method
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
