package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates {

    private double x,y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x,y);
    }

    public double x(){
        return x;
    }

    public double y(){
        return y;
    }

    @Override
    public final boolean equals(Object o){
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString(){ return String.format(Locale.ROOT,"abscisse=%.4f, ordonnée=%.4f", x(), y());}
}
