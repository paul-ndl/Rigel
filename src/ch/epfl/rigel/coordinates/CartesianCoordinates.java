package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Des coordonnées cartésiennes
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class CartesianCoordinates {

    private final double x,y;

    /**
     * Construit des coordonnées cartésiennes
     * @param x
     *          l'abscisse
     * @param y
     *          l'ordonnée
     */
    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Construit des coordonnées cartésiennes
     * @param x
     *          l'abscisse
     * @param y
     *          l'ordonnée
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x, y);
    }

    /**
     * Retourne l'abscisse
     * @return l'abscisse
     */
    public double x(){
        return x;
    }

    /**
     * Retourne l'ordonnée
     * @return l'ordonnée
     */
    public double y(){
        return y;
    }

    /**
     * Empêche d'utiliser cette méthode
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

    /**
     * Retourne une représentation textuelle des coordonnées (précision à 4 décimales)
     * @return une représentation textuelle des coordonnées (précision à 4 décimales)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"(abs=%.4f, ord=%.4f)", x, y);
    }

}
