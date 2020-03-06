package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinatesB {

    private double x,y;

    private CartesianCoordinatesB(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static CartesianCoordinatesB of(double x, double y){
        return new CartesianCoordinatesB(x, y);
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
    public String toString(){
        return String.format(Locale.ROOT,"(abs=%.4f°, ord=%.4f°)", x, y);
    }

}
