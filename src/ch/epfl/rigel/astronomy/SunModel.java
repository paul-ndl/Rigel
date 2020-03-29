package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Un modèle du Soleil
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

    private final static double lonJ2010 = Angle.ofDeg(279.557208);
    private final static double lonPer = Angle.ofDeg(283.112438);
    private final static double e = 0.016705;
    private final static double teta0 = Angle.ofDeg(0.533128);

    /**
     * Retourne le Soleil modélisé pour les arguments donnés
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     * @param daysSinceJ2010
     *             le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion
     *             la conversion
     * @return le Soleil modélisé pour les arguments donnés
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        final double m = (Angle.TAU/365.242191) * daysSinceJ2010 + lonJ2010 - lonPer;
        final double nu = m + 2*e*Math.sin(m);
        final double angularSize = teta0 * ((1+e*Math.cos(nu))/(1-e*e));
        final double lambda = Angle.normalizePositive(nu + lonPer);
        final double beta = 0;
        final EclipticCoordinates ec = EclipticCoordinates.of(lambda, beta);
        final EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec);
        return new Sun(ec, eq, (float) angularSize, (float) m);
    }
}
