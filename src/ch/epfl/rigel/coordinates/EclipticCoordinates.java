package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates {

    private EclipticCoordinates(double eclipticLongitude, double eclipticLatitude){
        super(eclipticLongitude, eclipticLatitude);
    }

    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkArgument(isValidLonDeg(Angle.toDeg(lon)) && isValidLatDeg(Angle.toDeg(lat)));
        return new EclipticCoordinates(lon, lat);
    }

    public static boolean isValidLonDeg(double lonDeg){
        RightOpenInterval eclipticLongitudeIn = RightOpenInterval.of(0, 360);
        return eclipticLongitudeIn.contains(lonDeg);
    }

    public static boolean isValidLatDeg(double latDeg){
        ClosedInterval eclipticLatitudeIn = ClosedInterval.symmetric(180);
        return eclipticLatitudeIn.contains(latDeg);
    }

    public double lon(){
        return super.lon();
    }

    public double lonDeg(){
        return super.lonDeg();
    }

    public double lat(){
        return super.lat();
    }

    public double latDeg(){
        return super.latDeg();
    }

    public String toString(){
        return String.format(Locale.ROOT,"(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }

}
