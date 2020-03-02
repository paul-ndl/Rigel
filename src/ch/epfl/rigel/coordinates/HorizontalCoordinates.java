package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * constructs horizontal coordinates with the given azimut and altitude
     */
    private HorizontalCoordinates(double azimut, double altitude){
        super(azimut, altitude);
    }

    /**
     * constructs horizontal coordinates with the given azimut and altitude (in radians) if they are correct
     * throws exception otherwise
     */
    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkArgument(isValidAzDeg(Angle.toDeg(az)) && isValidAltDeg(Angle.toDeg(alt)));
        return new HorizontalCoordinates(az, alt);
    }

    /**
     * constructs horizontal coordinates with the given azimut and altitude (in degrees) if they are correct
     * throws exception otherwise
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        Preconditions.checkArgument(isValidAzDeg(azDeg) && isValidAltDeg(altDeg));
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * checks if the azimut is in the good interval
     */
    public static boolean isValidAzDeg(double azDeg){
        RightOpenInterval azimutIn = RightOpenInterval.of(0, 360);
        return azimutIn.contains(azDeg);
    }

    /**
     * checks if the altitude is in the good interval
     */
    public static boolean isValidAltDeg(double altDeg){
        ClosedInterval altitudeIn = ClosedInterval.symmetric(180);
        return altitudeIn.contains(altDeg);
    }

    /**
     * returns the azimut in radians
     */
    public double az(){
        return super.lon();
    }

    /**
     * returns the azimut in degrees
     */
    public double azDeg(){
        return  super.lonDeg();
    }

    /**
     * returns the direction depending on the azimut
     */
    public String azOctantName(String n, String e, String s, String w){
        String azOctant = new String();
        if (RightOpenInterval.of(0, 22.5).contains(azDeg()) || RightOpenInterval.of(337.5, 360).contains(azDeg())){
            azOctant = n;
        }
        if (RightOpenInterval.of(22.5, 67.5).contains(azDeg())){
            azOctant = n+e;
        }
        if (RightOpenInterval.of(67.5, 112.5).contains(azDeg())){
            azOctant = e;
        }
        if (RightOpenInterval.of(112.5, 157.5).contains(azDeg())){
            azOctant = s+e;
        }
        if (RightOpenInterval.of(157.5, 202.5).contains(azDeg())){
            azOctant = s;
        }
        if (RightOpenInterval.of(202.5, 247.5).contains(azDeg())){
            azOctant = s+w;
        }
        if (RightOpenInterval.of(247.5, 292.5).contains(azDeg())){
            azOctant = w;
        }
        if (RightOpenInterval.of(292.5, 337.5).contains(azDeg())){
            azOctant = n+w;
        }
        return azOctant;
    }

    /**
     * returns the altitude in radians
     */
    public double alt(){
        return super.lat();
    }

    /**
     * returns the altitude in degrees
     */
    public double altDeg(){
        return super.latDeg();
    }

    /**
     * returns the angular distance from these coordinates to the coordinates given in argument
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        double angularDistance;
        angularDistance = Math.acos(Math.sin(this.alt()) * Math.sin(that.alt()) + Math.cos(this.alt()) * Math.cos(that.alt()) * (Math.cos(this.az() - that.az())));
        return angularDistance;
    }

    /**
     * returns a string representation of the coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(az=%.4f°, alt=%.4f°)", lonDeg(), latDeg());
    }

}
