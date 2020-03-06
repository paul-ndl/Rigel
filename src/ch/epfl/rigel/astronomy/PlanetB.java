package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public final class PlanetB extends CelestialObjectB {

    public PlanetB(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        super(name, equatorialPos, angularSize, magnitude);
    }
}
