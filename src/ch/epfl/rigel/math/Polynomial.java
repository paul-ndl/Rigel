package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

import static java.lang.System.arraycopy;

/**
 * Un Polynôme
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun
 */
public final class Polynomial {

    private double[] coefficients;

    /**
     * Construit un polynôme
     * @param coefficientN
     *          le coefficient de plus grand degré
     * @param coefficients
     *          la liste des autres coefficients
     */
    private Polynomial (double coefficientN, double... coefficients){
        this.coefficients = new double[coefficients.length+1];
        this.coefficients[0] = coefficientN;
        arraycopy(coefficients, 0, this.coefficients, 1, coefficients.length);
    }

    /**
     * Construit un polynôme
     * @param coefficientN
     *          le coefficient de plus grand degré
     * @param coefficients
     *          la liste des autres coefficients
     * @throws IllegalArgumentException
     *          si le coefficient de plus grand degré est nul
     */
    public static Polynomial of(double coefficientN, double... coefficients){
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * Retourne la valeur du polynôme pour l'argument donné (forme de Horner)
     * @param x
     *          l'argument
     * @return la valeur du polynôme pour l'argument donné
     */
    public double at(double x){
        double result = 0;
        for(int i=0; i<coefficients.length; ++i){
            result = result * x + coefficients[i];
        }
        return result;
    }

    /**
     * Retourne une représentation textuelle du polynôme
     * @return une représentation textuelle du polynôme
     */
    public String toString(){
        int power = coefficients.length-1;
        StringBuilder polynomial = new StringBuilder();
        for (int i=0; i<coefficients.length; ++i){
            if (coefficients[i]!=0) {
                if (coefficients[i] > 0 && i!=0) {
                    polynomial.append("+");
                }
                if (coefficients[i] < 0) {
                    polynomial.append("-");
                }
                if (coefficients[i] != 1 && coefficients[i] != -1) {
                    polynomial.append(String.format(Locale.ROOT, "%s", Math.abs(coefficients[i])));
                }
                if (power != 0) {
                    polynomial.append("x");
                    if (power != 1) {
                        polynomial.append(String.format(Locale.ROOT, "^%s", power));
                    }
                }
            }
            --power;
        }
        return polynomial.toString();
    }

    /**
     * Empêche d'utiliser cette méthode
     * @param o
     *          l'objet
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
