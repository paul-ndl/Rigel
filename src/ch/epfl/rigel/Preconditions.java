package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Vérifie si une condition est vraie ou fausse
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * @param isTrue
     *          argument à vérifier
     * @throws IllegalArgumentException
     *          si l'argument est faux
     */
    public static void checkArgument(boolean isTrue){
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retourne la valeur
     * @param interval
     *          l'intervalle
     * @param value
     *          la valeur
     * @throws IllegalArgumentException
     *          si la valeur n'appartient pas à l'intervalle
     * @return la valeur
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}
