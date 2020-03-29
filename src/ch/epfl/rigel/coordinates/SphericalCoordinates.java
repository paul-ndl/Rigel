package ch.epfl.rigel.coordinates;


import ch.epfl.rigel.math.Angle;

/**
 * Des coordonnées sphériques
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
abstract class SphericalCoordinates {

    private double longitude;
    private double latitude;

    /**
     * Construit des coordonnées sphériques
     * @param longitude
     *          la longitude
     * @param latitude
     *          la latitude
     */
    SphericalCoordinates (double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Retourne la longitude en radians
     * @return la longitude en radians
     */
    double lon(){
        return this.longitude;
    }

    /**
     * Retourne la longitude en degrés
     * @return la longitude en degrés
     */
    double lonDeg(){
        return Angle.toDeg(this.longitude);
    }

    /**
     * Retourne la latitude en radians
     * @return la latitude en radians
     */
    double lat(){
        return this.latitude;
    }

    /**
     * Retourne la latitude en degrés
     * @return la latitude en degrés
     */
    double latDeg(){
        return Angle.toDeg(this.latitude);
    }

    /**
     * Empêche d'utiliser cette méthode
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * Empêche d'utiliser cette méthode
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }
}
