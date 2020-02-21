package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

import static java.lang.System.arraycopy;

public final class Polynomial {

    private double[] coefficients;

    private Polynomial (double coefficientN, double... coefficients){
        this.coefficients = new double[coefficients.length+1];
        this.coefficients[0] = coefficientN;
        arraycopy(coefficients, 0, this.coefficients, 1, coefficients.length);
    }

    public static Polynomial of(double coefficientN, double... coefficients){
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    public double at(double x){
        double result = 0;
        for(int i=0; i<coefficients.length; ++i){
            result = result * x + coefficients[i];
        }
        return result;
    }

    public String toString(){
        int power = coefficients.length-1;
        StringBuilder polynome = new StringBuilder();
        for (int i=0; i<coefficients.length; ++i){
            if (coefficients[i]!=0) {
                if (coefficients[i] > 0 && i!=0) {
                    polynome.append("+");
                }
                if (coefficients[i] < 0) {
                    polynome.append("-");
                }
                if (coefficients[i] != 1 && coefficients[i] != -1) {
                    polynome.append(String.format(Locale.ROOT, "%s", Math.abs(coefficients[i])));
                }
                if (power != 0) {
                    polynome.append("x");
                    if (power != 1) {
                        polynome.append(String.format(Locale.ROOT, "^%s", power));
                    }
                }
            }
            --power;
        }
        return polynome.toString();
    }


    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

}
