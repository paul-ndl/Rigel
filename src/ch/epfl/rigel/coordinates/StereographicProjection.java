package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private double center;

    public StereographicProjection(HorizontalCoordinates center){
        this.center = center;
        return new StereographicProjection(center)
    }

    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double x = 0;
        double y = (Math.cos(phy)) / (Math.sin(hor.alt()) + Math.sin(phy));
        return CartesianCoordinates.of(x, y);
    }

    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        double p = (Math.cos(parallel.alt())) / (Math.cos(parallel.alt())+Math.sin(phy));
        return p;
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
    public String toString(){ return String.format(Locale.ROOT,"les coordonnées du centre de la projection sont ", circleCenterForParallel());}
}
