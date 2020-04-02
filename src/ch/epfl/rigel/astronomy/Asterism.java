package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * Un astérisme
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Asterism {

    private final List<Star> stars;

    /**
     * Construit un astérisme
     * @param stars
     *          la liste d'étoiles constituant l'astérisme
     * @throws IllegalArgumentException
     *          si la liste d'étoiles est vide
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars);
    }

    /**
     * Retourne la liste d'étoiles
     * @return la liste d'étoiles
     */
    public List<Star> stars(){
        return stars;
    }
}
