package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public abstract class CelestialObject {

    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize, magnitude;

    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        Preconditions.checkArgument(angularSize>=0);
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    public String name(){
        return name;
    }

    public double angularSize(){
        return angularSize;
    }

    public double magnitude(){
        return magnitude;
    }

    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    public String info(){
        return name;
    }

    @Override
    public String toString(){
        return info();
    }

}
