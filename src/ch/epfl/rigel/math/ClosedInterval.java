package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Un intervalle fermé
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class ClosedInterval extends Interval {

    /**
     * Construit un intervalle fermé
     * @param low
     *          la borne inférieure
     * @param high
     *          la borne supérieure
     */
    private ClosedInterval (double low, double high){
        super(low, high);
    }


    /**
     * Construit un intervalle fermé allant de la borne inférieure à la borne supérieure
     * @param low
     *          la borne inférieure
     * @param high
     *          la borne supérieure
     * @throws IllegalArgumentException
     *          si la borne inférieure n'est pas strictement inférieure à la borne supérieure
     * @return un intervalle fermé allant de la borne inférieure à la borne supérieure
     */
    public static ClosedInterval of(double low, double high){
        Preconditions.checkArgument(high>low);
        return new ClosedInterval(low, high);
    }

    /**
     * Construit un intervalle fermé centré en 0 et de taille donnée
     * @param size
     *          la taille
     * @throws IllegalArgumentException
     *          si la taille n'est pas strictement positive
     * @return un intervalle fermé centré en 0 et de taille donnée
     */
    public static ClosedInterval symmetric(double size){
        double symmetricBound = size/2;
        Preconditions.checkArgument(size>0);
        return new ClosedInterval(-symmetricBound,symmetricBound);
    }

    /**
     * Vérifie que la valeur appartient à l'intervalle
     * @param v
     *          la valeur
     * @return vrai si la valeur appartient à l'intervalle
     */
    @Override
    public boolean contains(double v) {
        return (v>=super.low() && v<=super.high());
    }

    /**
     * Ecrête la valeur à l'intervalle
     * @param v
     *          la valeur
     * @return la valeur écrêtée
     */
    public double clip (double v){
        if (v<super.low()){
            return super.low();
        } else if (v>super.high()){
            return super.high();
        } else {
            return v;
        }
    }

    /**
     * Retourne une représentation textuelle de l'intervalle (bornes)
     * @return une représentation textuelle de l'intervalle (bornes)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"[%s,%s]",super.low(),super.high());
    }
}
