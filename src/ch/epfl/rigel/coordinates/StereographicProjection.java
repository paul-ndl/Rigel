package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Une projection stéréographique
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double lambdaCenter, phyCenter;
    private final double phyCos, phySin;

    /**
     * Construit une projection stéréographique centrée sur les coordonnées données
     * @param center
     *          les coordonnées du centre
     */
    public StereographicProjection(HorizontalCoordinates center){
        lambdaCenter = center.az();
        phyCenter = center.alt();
        phyCos = Math.cos(phyCenter);
        phySin = Math.sin(phyCenter);
    }

    /**
     * Retourne les coordonnées du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     * @param hor
     *          les coordonnées du point
     * @return les coordonnées du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        final double x = 0;
        final double y = phyCos/(Math.sin(hor.alt()) + phySin);
        return CartesianCoordinates.of(x,y);
    }

    /**
     * Retourne le rayon du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     * @param parallel
     *          les coordonnées du point
     * @return le rayon du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return Math.cos(parallel.alt())/(Math.sin(parallel.alt()) + phySin);
    }

    /**
     * Retourne le diamètre projeté d'une sphère de taille angulaire donnée
     * @param rad
     *          la taille angulaire
     * @return le diamètre projeté d'une sphère de taille angulaire donnée
     */
    public double applyToAngle(double rad){
        return 2*Math.tan(rad/4);
    }

    /**
     * Retourne les coordonnées cartésiennes de la projection
     * des coordonnées horizontales données
     * @param azAlt
     *          les coordonnées horizontales
     * @return les coordonnées cartésiennes de la projection
     * des coordonnées horizontales données
     */
    public CartesianCoordinates apply(HorizontalCoordinates azAlt){
        final double delta = azAlt.az()-lambdaCenter;
        final double d = (double) 1/(1 + Math.sin(azAlt.alt())*phySin + Math.cos(azAlt.alt())*phyCos*Math.cos(delta));
        final double x = d * Math.cos(azAlt.alt()) * Math.sin(delta);
        final double y = d * (Math.sin(azAlt.alt()) * phyCos - Math.cos(azAlt.alt()) * phySin * Math.cos(delta));
        return CartesianCoordinates.of(x,y);
    }

    /**
     * Retourne les coordonnées horizontales du point dont la projection
     * est le point de coordonnées cartésiennes données
     * @param xy
     *          les coordonnées cartésiennes
     * @return les coordonnées horizontales du point dont la projection
     * est le point de coordonnées cartésiennes données
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        final double p = Math.sqrt(xy.x()*xy.x() + xy.y()*xy.y());
        final double sinc = (2 * p) / (p * p + 1);
        final double cosc = (1 - p * p) / (p * p + 1);
        final double lambda = Angle.normalizePositive(Math.atan2(xy.x()*sinc, p*phyCos*cosc - xy.y()*phySin*sinc) + lambdaCenter);
        final double phy = Math.asin(cosc*phySin + ((xy.y()*sinc*phyCos)/p));
        return HorizontalCoordinates.of(lambda, phy);
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
     * Retourne une représentation textuelle de la projection stéréographique (centre)
     * @return une représentation textuelle de la projection stéréographique (centre)
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT,"StereographicProjection => Center Coordinates (λ=%.4f, ϕ=%.4f)", lambdaCenter, phyCenter);
    }

}
