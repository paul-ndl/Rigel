package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Un intervalle ouvert à droite
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class RightOpenInterval extends Interval {

    /**
     * Construit un intervalle ouvert à droite
     *
     * @param low  la borne inférieure
     * @param high la borne supérieure
     */
    private RightOpenInterval(double low, double high) {
        super(low, high);
    }

    /**
     * Construit un intervalle ouvert à droite allant de la borne inférieure à la borne supérieure
     *
     * @param low  la borne inférieure
     * @param high la borne supérieure
     * @return un intervalle ouvert à droite allant de la borne inférieure à la borne supérieure
     * @throws IllegalArgumentException si la borne inférieure n'est pas strictement inférieure à la borne supérieure
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(high > low);
        return new RightOpenInterval(low, high);
    }

    /**
     * Construit un intervalle ouvert à droite centré en 0 et de taille donnée
     *
     * @param size la taille
     * @return un intervalle ouvert à droite centré en 0 et de taille donnée
     * @throws IllegalArgumentException si la taille n'est pas strictement positive
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        double bound = size / 2;
        return new RightOpenInterval(-bound, bound);
    }

    /**
     * Vérifie que la valeur appartient à l'intervalle
     *
     * @param v la valeur
     * @return vrai si la valeur appartient à l'intervalle
     */
    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<super.high());
    }

    /**
     * Retourne le reste de la partie entière par défaut de x/y
     *
     * @param x le numérateur
     * @param y la dénominateur
     * @return le reste de la partie entière par défaut de x/y
     */
    private double floorMod(double x, double y) {
        return x - y*Math.floor(x/y);
    }

    /**
     * Réduit la valeur à l'intervalle
     *
     * @param v la valeur
     * @return la valeur réduite
     */
    public double reduce(double v) {
        return super.low() + floorMod(v-super.low(), super.size());
    }

    /**
     * Retourne une représentation textuelle de l'intervalle (bornes)
     *
     * @return une représentation textuelle de l'intervalle (bornes)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                     "[%s,%s[",
                             super.low(),
                             super.high());
    }

}
