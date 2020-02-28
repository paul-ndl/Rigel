package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates {

    /**
     * constructs geographic coordinates with the given longitude and latitude
     */
    private EclipticCoordinates(double eclipticLongitude, double eclipticLatitude){
        super(eclipticLongitude, eclipticLatitude);
    }

    /**
     * constructs horizontal coordinates with the given longitude and latitude (in radians) if they are correct
     * throws exception otherwise
     */
    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkArgument(isValidLonDeg(Angle.toDeg(lon)) && isValidLatDeg(Angle.toDeg(lat)));
        return new EclipticCoordinates(lon, lat);
    }

    /**
     * checks if the longitude is in the good interval
     */
    public static boolean isValidLonDeg(double lonDeg){
        RightOpenInterval eclipticLongitudeIn = RightOpenInterval.of(0, 360);
        return eclipticLongitudeIn.contains(lonDeg);
    }

    /**
     * checks if the latitude is in the good interval
     */
    public static boolean isValidLatDeg(double latDeg){
        ClosedInterval eclipticLatitudeIn = ClosedInterval.symmetric(180);
        return eclipticLatitudeIn.contains(latDeg);
    }

    /**
     * redefine the method from the super class
     * returns the longitude in radians
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * redefine the method from the super class
     * returns the longitude in degrees
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * redefine the method from the super class
     * returns the latitude in radians
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * redefine the method from the super class
     * returns the longitude in degrees
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * returns a string representation of the coordinates
     */
    public String toString(){
        return String.format(Locale.ROOT,"(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }

}
