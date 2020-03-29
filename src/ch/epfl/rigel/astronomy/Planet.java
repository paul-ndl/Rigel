package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Une planète
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Planet extends CelestialObject {

    /**
     * Construit une planète
     * @param name
     *          le nom
     * @param equatorialPos
     *          la position équatoriale
     * @param angularSize
     *          la taille angulaire
     * @param magnitude
     *          la magnitude
     * @see CelestialObject#CelestialObject(String, EquatorialCoordinates, float, float)
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        super(name, equatorialPos, angularSize, magnitude);
    }

}
