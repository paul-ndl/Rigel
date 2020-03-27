package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

    private final static double lonJ2010 = Angle.ofDeg(279.557208);
    private final static double lonPer = Angle.ofDeg(283.112438);
    private final static double e = 0.016705;
    private final static double teta0 = Angle.ofDeg(0.533128);

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        double m = (Angle.TAU/365.242191) * daysSinceJ2010 + lonJ2010 - lonPer;
        double nu = m + 2*e*Math.sin(m);
        double angularSize = teta0 * ((1+e*Math.cos(nu))/(1-e*e));
        double lambda = Angle.normalizePositive(nu + lonPer);
        double beta = 0;
        EclipticCoordinates ec = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec);
        return new Sun(ec, eq, (float) angularSize, (float) m);
    }
}
