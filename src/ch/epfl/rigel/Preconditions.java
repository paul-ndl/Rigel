package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Une précondition
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Lance une exception si l'argument est faux
     *
     * @param isTrue argument à vérifier
     * @throws IllegalArgumentException si l'argument est faux
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue)
            throw new IllegalArgumentException();
    }

    /**
     * Retourne la valeur
     *
     * @param interval l'intervalle
     * @param value    la valeur
     * @return la valeur
     * @throws IllegalArgumentException si la valeur n'appartient pas à l'intervalle
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}
