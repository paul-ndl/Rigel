package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EquatorialCoordinates extends SphericalCoordinates {

    /**
     * constructs equatorial coordinates with the given rigth ascension and declination
     */
    private EquatorialCoordinates(double rightAscension, double declination){
        super(rightAscension, declination);
    }

    /**
     * constructs equatorial coordinates with the given right ascension and declination (in radians) if they are correct
     * throws exception otherwise
     */
    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkArgument(isValidRaDeg(Angle.toDeg(ra)) && isValidDecDeg(Angle.toDeg(dec)));
        return new EquatorialCoordinates(ra, dec);
    }

    /**
     * checks if the right ascension is in the good interval
     */
    public static boolean isValidRaDeg(double raDeg){
        RightOpenInterval rightAscensionIn = RightOpenInterval.of(0, 360);
        return rightAscensionIn.contains(raDeg);
    }

    /**
     * checks if the declination is in the good interval
     */
    public static boolean isValidDecDeg(double decDeg){
        ClosedInterval declinationIn = ClosedInterval.symmetric(180);
        return declinationIn.contains(decDeg);
    }

    /**
     * returns the right ascension in radians
     */
    public double ra(){
        return super.lon();
    }

    /**
     * returns the right ascension in degrees
     */
    public double raDeg(){
        return  super.lonDeg();
    }

    /**
     * returns the right ascension in hours
     */
    public double raHr(){
        return  Angle.toHr(super.lon());
    }

    /**
     * returns the declination in radians
     */
    public double dec(){
        return super.lat();
    }

    /**
     * returns the declination in degrees
     */
    public double decDeg(){
        return super.latDeg();
    }

    /**
     * returns a string representation of the coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }

}
