package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private CartesianCoordinates center;
    private double lambda, phy;

    public StereographicProjection(HorizontalCoordinates center){
        lambda = center.az();
        phy = center.alt();
        this.center = CartesianCoordinates.of(center.az(), center.alt());
    }

    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double x = 0;
        double y = (Math.cos(phy)) / (Math.sin(hor.alt()) + Math.sin(phy));
        return CartesianCoordinates.of(x, y);
    }

    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        double radius = (Math.cos(parallel.alt())) / (Math.sin(parallel.alt())+Math.sin(phy));
        return radius;
    }

    public double applyToAngle(double rad){
        double diameter = 2*Math.tan(rad/4);
        return diameter;
    }

    public CartesianCoordinates apply(HorizontalCoordinates azAlt){
        double delta = azAlt.az()-lambda;
        double d = (1) / (1 + Math.sin(azAlt.alt())*Math.sin(phy) + Math.cos(azAlt.alt()*Math.cos(phy)*Math.cos(delta)));
        double x = d * Math.cos(azAlt.alt() * Math.sin(delta));
        double y = d * (Math.sin(azAlt.alt())*Math.cos(phy) - Math.cos(azAlt.alt()*Math.sin(phy)*Math.cos(delta)));
        return CartesianCoordinates.of(x, y);
    }

    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double p = Math.sqrt(xy.x()*xy.x() + xy.y()*xy.y());
        double sinc = (2*p)/(p*p+1);
        double cosc = (1-p*p)/(p*p+1);
        double x = xy.x();
        double y = xy.y();
        double longitude = Math.atan2(x*sinc, p*Math.cos(phy)*cosc - y*Math.sin(phy)*sinc) + lambda;
        double latitude = Math.asin((cosc*Math.sin(phy)) + ((y*sinc*Math.cos(phy))/p));
        return HorizontalCoordinates.of(longitude, latitude);
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
    public String toString(){ return String.format(Locale.ROOT,"(lon=%.4f, lat=%.4f)", lambda, phy); }
}
