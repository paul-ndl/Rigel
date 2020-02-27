package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class GeographicCoordinates extends SphericalCoordinates{

    private GeographicCoordinates(double longitude, double latitude){
        super(longitude, latitude);
    }

    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkArgument(isValidLonDeg(lonDeg) && isValidLatDeg(latDeg));
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    public static boolean isValidLonDeg(double lonDeg){
        RightOpenInterval longitudeIn = RightOpenInterval.symmetric(360);
        return longitudeIn.contains(lonDeg);
    }

    public static boolean isValidLatDeg(double latDeg){
        ClosedInterval latitudeIn = ClosedInterval.symmetric(180);
        return latitudeIn.contains(latDeg);
    }

    @Override
    public double lon(){
        return super.lon();
    }

    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    @Override
    public double lat(){
        return super.lat();
    }

    @Override
    public double latDeg(){
        return super.latDeg();
    }

    public String toString(){
        return String.format(Locale.ROOT,"(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }

}
