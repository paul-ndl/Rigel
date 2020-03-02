package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public abstract class CelestialObject {

    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize, magnitude;

    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        if(angularSize < 0){
            throw new IllegalArgumentException;
        }//ici trouver comment utiliser 'Objects.requireNonNull'

        if(name==null || equatorialPos==null){
            throw new NullPointerException;
        }
        this.name=name;
        this.equatorialPos=equatorialPos;
        this.angularSize=angularSize;
        this.magnitude=magnitude;
    }

    public String name(){
        return  name;
    }

    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    public double angularSize(){
       return angularSize;
    }
    public double magnitude(){
        return magnitude;
    }
    //retourne une nouvelle chaine de caractère
    public abstract String info();

    public String toString(){
        return info();
    }
}
