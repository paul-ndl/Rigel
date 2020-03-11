package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public final class Sun extends CelestialObject {

    private static final float MAGNITUDE = (float)-26.7;
    private EclipticCoordinates eclipticPos;
    private float meanAnomaly;

    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly){
        super("Soleil", equatorialPos, angularSize, MAGNITUDE);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    public double meanAnomaly(){
        return  meanAnomaly;
    }
}
