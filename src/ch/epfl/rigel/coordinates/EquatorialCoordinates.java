package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates {

    private EquatorialCoordinates(double rightAscension, double declination){
        super(rightAscension, declination);
    }

    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkArgument(isValidRaDeg(Angle.toDeg(ra)) && isValidDecDeg(Angle.toDeg(dec)));
        return new EquatorialCoordinates(ra, dec);
    }

    public static boolean isValidRaDeg(double raDeg){
        RightOpenInterval rightAscensionIn = RightOpenInterval.of(0, 360);
        return rightAscensionIn.contains(raDeg);
    }

    public static boolean isValidDecDeg(double decDeg){
        ClosedInterval declinationIn = ClosedInterval.symmetric(180);
        return declinationIn.contains(decDeg);
    }


    public double ra(){
        return super.lon();
    }

    public double raDeg(){
        return  super.lonDeg();
    }

    public double raHr(){
        return  Angle.toHr(super.lon());
    }

    public double dec(){
        return super.lat();
    }

    public double decDeg(){
        return super.latDeg();
    }

    public String toString(){
        return String.format(Locale.ROOT,"(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }

}
