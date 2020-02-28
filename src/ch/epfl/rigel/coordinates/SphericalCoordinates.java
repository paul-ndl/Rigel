package ch.epfl.rigel.coordinates;


import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {

    private double longitude;
    private double latitude;

    /**
     * constructs spherical coordinates with the given longitude and latitude
     */
    SphericalCoordinates (double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * returns the longitude in radians
     */
    double lon(){
        return this.longitude;
    }

    /**
     * returns the longitude in degrees
     */
    double lonDeg(){
        return Angle.toDeg(this.longitude);
    }

    /**
     * returns the latitude in radians
     */
    double lat(){
        return this.latitude;
    }

    /**
     * returns the latitude in degrees
     */
    double latDeg(){
        return Angle.toDeg(this.latitude);
    }

    /**
     * prevents to use this method
     */
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * prevents to use this method
     */
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }
}
