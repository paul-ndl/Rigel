package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

public final class StereographicProjectionB implements Function<HorizontalCoordinates, CartesianCoordinatesB> {

    private double lambdaCenter, phyCenter;
    private double phyCos, phySin;

    public StereographicProjectionB(HorizontalCoordinates center){
        lambdaCenter = center.az();
        phyCenter = center.alt();
        phyCos = Math.cos(phyCenter);
        phySin = Math.sin(phyCenter);
    }

    public CartesianCoordinatesB circleCenterForParallel(HorizontalCoordinates hor){
        double x = 0;
        double y = phyCos/(Math.sin(hor.alt()) + phySin);
        return CartesianCoordinatesB.of(x,y);
    }

    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + phySin);
    }

    public double applyToAngle(double rad){
        return 2*Math.tan(rad/4);
    }

    public CartesianCoordinatesB apply(HorizontalCoordinates azAlt){
        double delta = azAlt.az()-lambdaCenter;
        double d = (double) 1/(1 + Math.sin(azAlt.alt())*phySin + Math.cos(azAlt.alt())*phyCos*Math.cos(delta));
        double x = d * Math.cos(azAlt.alt()) * Math.sin(delta);
        double y = d * (Math.sin(azAlt.alt()) * phyCos - Math.cos(azAlt.alt()) * phySin * Math.cos(delta));
        return CartesianCoordinatesB.of(x,y);
    }

    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double p = Math.sqrt(xy.x()*xy.x() + xy.y()*xy.y());
        double sinc = (2 * p) / (p * p + 1);
        double cosc = (1 - p * p) / (p * p + 1);
        double lambda = Angle.normalizePositive(Math.atan2(xy.x(), p*phyCos*cosc - xy.y()*phySin*sinc) + lambdaCenter);
        double phy = Math.asin(cosc*phySin + (xy.y()*sinc*phyCos) / p);
        return HorizontalCoordinates.of(lambda, phy);
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
        return String.format(Locale.ROOT,"StereographicProjection => Center Coordinates (λ=%.4f°, ϕ=%.4f°)", lambdaCenter, phyCenter);
    }

}
