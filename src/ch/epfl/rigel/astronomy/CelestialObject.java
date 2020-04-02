package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Un objet céleste
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize, magnitude;

    /**
     * Construit un objet céleste
     * @param name
     *          le nom
     * @param equatorialPos
     *          la position équatoriale
     * @param angularSize
     *          la taille angulaire
     * @param magnitude
     *          la magnitude
     * @throws IllegalArgumentException
     *          si la taille angulaire est négative
     * @throws NullPointerException
     *          si le nom ou la position équatoriale sont nuls
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        Preconditions.checkArgument(angularSize>=0);
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * Retourne le nom
     * @return le nom
     */
    public String name(){
        return name;
    }

    /**
     * Retourne la taille angulaire
     * @return la taille angulaire
     */
    public double angularSize(){
        return angularSize;
    }

    /**
     * Retourne la magnitude
     * @return la magnitude
     */
    public double magnitude(){
        return magnitude;
    }

    /**
     * Retourne la position équatoriale
     * @return la position équatoriale
     */
    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    /**
     * Retourne un texte informatif sur l'objet
     * @return le nom de l'objet
     */
    public String info(){
        return name();
    }

    /**
     * @see #info()
     */
    @Override
    public String toString(){
        return info();
    }

}
