package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){

        return new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), 1, 0);
    }
}
