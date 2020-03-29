package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import javax.naming.ServiceUnavailableException;

/**
 * Un modèle de la Lune
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON;

    private static final double l0 = Angle.ofDeg(91.929336);
    private static final double p0 = Angle.ofDeg(130.143076);
    private static final double n0 = Angle.ofDeg(291.682547);
    private static final double i = Angle.ofDeg(5.145396);
    private static final double e = 0.0549;
    private static final double theta0 = Angle.ofDeg(0.5181);

    /**
     * Retourne la Lune modélisée pour les arguments donnés
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     * @param daysSinceJ2010
     *             le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion
     *             la conversion
     * @return la Lune modélisée pour les arguments donnés
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        final double lambdaS = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).eclipticPos().lon();
        final double mS = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).meanAnomaly();
        final double l = Angle.ofDeg(13.1763966) * daysSinceJ2010 + l0;
        final double mM = l - Angle.ofDeg(0.1114041)*daysSinceJ2010 - p0;
        final double eV = Angle.ofDeg(1.2739) * Math.sin(2*(l-lambdaS) - mM);
        final double aE = Angle.ofDeg(0.1858) * Math.sin(mS);
        final double a3 = Angle.ofDeg(0.37) * Math.sin(mS);
        final double mMCorrected = mM + eV - aE - a3;
        final double eC = Angle.ofDeg(6.2886) * Math.sin(mMCorrected);
        final double a4 = Angle.ofDeg(0.214) * Math.sin(2*mMCorrected);
        final double lF1 = l + eV + eC -aE + a4;
        final double v = Angle.ofDeg(0.6583) * Math.sin(2*(lF1-lambdaS));
        final double orbitalLon = lF1 + v;
        final double n = n0 - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        final double nFinal = n - Angle.ofDeg(0.16)*Math.sin(mS);
        final double lambda = Angle.normalizePositive(Math.atan2(Math.sin(orbitalLon-nFinal)*Math.cos(i), Math.cos(orbitalLon-nFinal)) + nFinal);
        final double beta = Math.asin(Math.sin(orbitalLon-nFinal) * Math.sin(i));
        final EclipticCoordinates ec = EclipticCoordinates.of(lambda, beta);
        final double phase = (1-Math.cos(orbitalLon-lambdaS))/2;
        final double p = (1-e*e) / (1+e*Math.cos(mMCorrected+eC));
        final double angularSize = theta0/p;
        return new Moon(eclipticToEquatorialConversion.apply(ec), (float) angularSize, 0f, (float) phase);
    }


}
