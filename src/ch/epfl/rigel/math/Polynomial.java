package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

import static java.lang.System.arraycopy;

public final class Polynomial {

    private double[] coefficients;

    /**
     * constructs a polynomial with the given coefficients
     */
    private Polynomial (double coefficientN, double... coefficients){
        this.coefficients = new double[coefficients.length+1];
        this.coefficients[0] = coefficientN;
        arraycopy(coefficients, 0, this.coefficients, 1, coefficients.length);
    }

    /**
     * contructs a polynomial if the biggest coefficient is not null
     * throws exception otherwise
     */
    public static Polynomial of(double coefficientN, double... coefficients){
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * calculates the polynomial with the given value
     */
    public double at(double x){
        double result = 0;
        for(int i=0; i<coefficients.length; ++i){
            result = result * x + coefficients[i];
        }
        return result;
    }

    /**
     * returns a string representation of the polynomial
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
     * prevents to use this method
     */
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    /**
     * prevents to use this method
     */
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
