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
public final class  StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double lambdaCenter, phyCenter;
    private final double phyCos, phySin;

    /**
     * Construit une projection stéréographique centrée sur les coordonnées données
     *
     * @param center les coordonnées du centre
     */
    public StereographicProjection(HorizontalCoordinates center) {
        lambdaCenter = center.az();
        phyCenter = center.alt();
        phyCos = Math.cos(phyCenter);
        phySin = Math.sin(phyCenter);
    }

    /**
     * Retourne les coordonnées du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     *
     * @param hor les coordonnées du point
     * @return les coordonnées du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double x = 0;
        double y = phyCos / (Math.sin(hor.alt())+phySin);
        return CartesianCoordinates.of(x, y);
    }

    /**
     * Retourne le rayon du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     *
     * @param parallel les coordonnées du point
     * @return le rayon du centre du cercle correspondant à la projection
     * du parallèle passant par le point donné
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        double lat = parallel.alt();
        return Math.cos(lat) / (Math.sin(lat)+phySin);
    }

    /**
     * Retourne le diamètre projeté d'une sphère de taille angulaire donnée
     *
     * @param rad la taille angulaire
     * @return le diamètre projeté d'une sphère de taille angulaire donnée
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad/4);
    }

    /**
     * Retourne les coordonnées cartésiennes de la projection
     * des coordonnées horizontales données
     *
     * @param azAlt les coordonnées horizontales
     * @return les coordonnées cartésiennes de la projection
     * des coordonnées horizontales données
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double altCos = Math.cos(azAlt.alt());
        double altSin = Math.sin(azAlt.alt());
        double deltaCos = Math.cos(azAlt.az() - lambdaCenter);
        double deltaSin = Math.sin(azAlt.az() - lambdaCenter);
        double d = 1d / (1 + altSin*phySin + altCos*phyCos*deltaCos);
        double x = d * altCos * deltaSin;
        double y = d * (altSin*phyCos - altCos*phySin*deltaCos);
        return CartesianCoordinates.of(x, y);
    }

    /**
     * Retourne les coordonnées horizontales du point dont la projection
     * est le point de coordonnées cartésiennes données
     *
     * @param xy les coordonnées cartésiennes
     * @return les coordonnées horizontales du point dont la projection
     * est le point de coordonnées cartésiennes données
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x();
        double y = xy.y();
        double pSquared = x*x + y*y;
        double p = Math.sqrt(pSquared);
        double sinC = (2*p) / (pSquared + 1);
        double cosC = (1 - pSquared) / (pSquared + 1);
        double lambda;
        double phy;
        if(x==0 && y==0){
            lambda = lambdaCenter;
            phy = phySin;
        } else {
            lambda = Angle.normalizePositive(Math.atan2(x*sinC, p*phyCos*cosC - y*phySin*sinC) + lambdaCenter);
            phy = Math.asin(cosC*phySin + (y*sinC*phyCos / p));
        }

        return HorizontalCoordinates.of(lambda, phy);
    }

    /**
     * Empêche d'utiliser cette méthode
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Empêche d'utiliser cette méthode
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne une représentation textuelle de la projection stéréographique (centre)
     *
     * @return une représentation textuelle de la projection stéréographique (centre)
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                      "StereographicProjection => Center Coordinates (λ=%.4f, ϕ=%.4f)",
                             lambdaCenter,
                             phyCenter);
    }

}
