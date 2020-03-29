package ch.epfl.rigel.math;

/**
 * Un intervalle
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public abstract class Interval {

    private final double lowerBound, upperBound;

    /**
     * Construit un intervalle
     * @param lowerBound
     *          la borne inférieure
     * @param upperBound
     *          la borne supérieure
     */
    protected Interval (double lowerBound,double upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Retourne la borne inférieure
     * @return la borne inférieure
     */
    public double low(){
        return lowerBound;
    }

    /**
     * Retourne la borne supérieure
     * @return la borne supérieure
     */
    public double high(){
        return upperBound;
    }

    /**
     * Retourne la taille de l'intervalle
     * @return la taille de l'intervalle
     */
    public double size(){
        return (upperBound-lowerBound);
    }

    /**
     * Vérifie que la valeur appartient à l'intervalle
     * @param v
     *          la valeur
     * @return vrai si la valeur appartient à l'intervalle
     */
    public abstract boolean contains(double v);

    /**
     * Empêche d'utiliser cette méthode
     * @param o
     *          un objet
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * Empêche d'utiliser cette méthode
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
