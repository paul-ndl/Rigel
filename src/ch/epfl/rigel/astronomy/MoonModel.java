package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import javax.naming.ServiceUnavailableException;

public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON;

    private static final double l0 = Angle.ofDeg(91.929336);
    private static final double p0 = Angle.ofDeg(130.143076);
    private static final double n0 = Angle.ofDeg(291.682547);
    private static final double i = Angle.ofDeg(5.145396);
    private static final double e = 0.0549;
    private static final double theta0 = Angle.ofDeg(0.5181);

    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        double d = daysSinceJ2010;
        double lambdaS = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).eclipticPos().lon();
        double mS = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).meanAnomaly();
        double l = Angle.ofDeg(13.1763966) * d + l0;
        double mM = l - Angle.ofDeg(0.1114041)*d - p0;
        double eV = Angle.ofDeg(1.2739) * Math.sin(2*(l-lambdaS) - mM);
        double aE = Angle.ofDeg(0.1858) * Math.sin(mS);
        double a3 = Angle.ofDeg(0.37) * Math.sin(mS);
        double mMCorrected = mM + eV - aE - a3;
        double eC = Angle.ofDeg(6.2886) * Math.sin(mMCorrected);
        double a4 = Angle.ofDeg(0.214) * Math.sin(2*mMCorrected);
        double lF1 = l + eV + eC -aE + a4;
        double v = Angle.ofDeg(0.6583) * Math.sin(2*(lF1-lambdaS));
        double orbitalLon = lF1 + v;
        double n = n0 - Angle.ofDeg(0.0529539) * d;
        double nFinal = n - Angle.ofDeg(0.16)*Math.sin(mS);
        double lambda = Angle.normalizePositive(Math.atan2(Math.sin(orbitalLon-nFinal)*Math.cos(i), Math.cos(orbitalLon-nFinal)) + nFinal);
        double beta = Math.asin(Math.sin(orbitalLon-nFinal) * Math.sin(i));
        EclipticCoordinates ec = EclipticCoordinates.of(lambda, beta);
        double phase = (1-Math.cos(orbitalLon-lambdaS))/2;
        double p = (1-e*e) / (1+e*Math.cos(mMCorrected+eC));
        double angularSize = theta0/p;
        return new Moon(eclipticToEquatorialConversion.apply(ec), (float) angularSize, 0, (float) phase);
    }


}
