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

    private static final double CONSTANT_L = Angle.ofDeg(13.1763966);
    private static final double CONSTANT_MM = Angle.ofDeg(0.1114041);
    private static final double CONSTANT_EV = Angle.ofDeg(1.2739);
    private static final double CONSTANT_AE = Angle.ofDeg(0.1858);
    private static final double CONSTANT_A3 = Angle.ofDeg(0.37);
    private static final double CONSTANT_EC = Angle.ofDeg(6.2886);
    private static final double CONSTANT_A4 = Angle.ofDeg(0.214);
    private static final double CONSTANT_V = Angle.ofDeg(0.6583);
    private static final double CONSTANT_N = Angle.ofDeg(0.0529539);
    private static final double CONSTANT_NFINAL = Angle.ofDeg(0.16);


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
        final double lambdaSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).eclipticPos().lon();
        final double mSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).meanAnomaly();
        final double l = CONSTANT_L*daysSinceJ2010 + l0;
        final double mM = l - CONSTANT_MM*daysSinceJ2010 - p0;
        final double eV = CONSTANT_EV * Math.sin(2*(l-lambdaSun) - mM);
        final double aE = CONSTANT_AE * Math.sin(mSun);
        final double a3 = CONSTANT_A3 * Math.sin(mSun);
        final double mMCorrected = mM + eV - aE - a3;
        final double eC = CONSTANT_EC * Math.sin(mMCorrected);
        final double a4 = CONSTANT_A4 * Math.sin(2*mMCorrected);
        final double lCorrected = l + eV + eC - aE + a4;
        final double v = CONSTANT_V * Math.sin(2*(lCorrected-lambdaSun));
        final double orbitalLon = lCorrected + v;
        final double n = n0 - CONSTANT_N * daysSinceJ2010;
        final double nFinal = n - CONSTANT_NFINAL*Math.sin(mSun);
        final double lambda = Angle.normalizePositive(Math.atan2(Math.sin(orbitalLon-nFinal)*Math.cos(i), Math.cos(orbitalLon-nFinal)) + nFinal);
        final double beta = Math.asin(Math.sin(orbitalLon-nFinal) * Math.sin(i));
        final EclipticCoordinates ecl = EclipticCoordinates.of(lambda, beta);
        final double phase = (1 - Math.cos(orbitalLon-lambdaSun)) / 2;
        final double p = (1 - e*e) / (1 + e*Math.cos(mMCorrected+eC));
        final double angularSize = theta0/p;
        return new Moon(eclipticToEquatorialConversion.apply(ecl), (float) angularSize, 0f, (float) phase);
    }


}
