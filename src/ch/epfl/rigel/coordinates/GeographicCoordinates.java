package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates{

    /**
     * constructs geographic coordinates with the given longitude and latitude
     */
    private GeographicCoordinates(double longitude, double latitude){
        super(longitude, latitude);
    }

    /**
     * constructs geographic coordinates with the given longitude and latitude (in degrees) if they are correct
     * throws exception otherwise
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkArgument(isValidLonDeg(lonDeg) && isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * checks if the longitude is in the good interval
     */
    public static boolean isValidLonDeg(double lonDeg){
        RightOpenInterval longitudeIn = RightOpenInterval.symmetric(360);
        return longitudeIn.contains(lonDeg);
    }

    /**
     * checks if the latitude is in the good interval
     */
    public static boolean isValidLatDeg(double latDeg){
        ClosedInterval latitudeIn = ClosedInterval.symmetric(180);
        return latitudeIn.contains(latDeg);
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
     * returns the latitude in degrees
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * returns a string representation of the coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }

}
