package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum PlanetModel implements CelestialObjectModel<Planet> {
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    public static List<PlanetModel> ALL = Arrays.asList(PlanetModel.values());

    private String name;

    private double t, lonJ2010, lonPer, e, a, i, omega, teta0, magnitude;

    PlanetModel(String name, double t, double lonJ2010, double lonPer, double e, double a, double i, double omega, double teta0, double magnitude){
        this.name = name;
        this.t = t;
        this.lonJ2010 = Angle.ofDeg(lonJ2010);
        this.lonPer = Angle.ofDeg(lonPer);
        this.e = e;
        this.a = a;
        this.i = Angle.ofDeg(i);
        this.omega = Angle.ofDeg(omega);
        this.teta0 = Angle.ofArcsec(teta0);
        this.magnitude = magnitude;
    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        double r = orbitalCoordinates(daysSinceJ2010)[0];
        double l = orbitalCoordinates(daysSinceJ2010)[1];
        double psi = Math.asin(Math.sin(l-omega)*Math.sin(i));
        double rFinal = r * Math.cos(psi);
        double lFinal = Math.atan2(Math.sin(l-omega)*Math.cos(i), Math.cos(l-omega)) + omega;
        double rEarth = EARTH.orbitalCoordinates(daysSinceJ2010)[0];
        double lEarth = EARTH.orbitalCoordinates(daysSinceJ2010)[1];
        double lambda, beta;
        if(a<1){
            lambda = Angle.normalizePositive(Math.PI + lEarth + Math.atan2(rFinal*Math.sin(lEarth-lFinal), rEarth-rFinal*Math.cos(lEarth-lFinal)));
        } else {
            lambda = Angle.normalizePositive(lFinal + Math.atan2(rEarth*Math.sin(lFinal-lEarth), rFinal-rEarth*Math.cos(lFinal-lEarth)));
        }
        beta = Math.atan((rFinal*Math.tan(psi)*Math.sin(lambda-lFinal))/(rEarth*Math.sin(lFinal-lEarth)));
        double p = Math.sqrt(rEarth*rEarth + r*r - 2*rEarth*r*Math.cos(l-lEarth)*Math.cos(psi));
        double angularSize = teta0/p;
        double f = (1+Math.cos(lambda-l))/2;
        double magnitudeF = magnitude + 5*Math.log10(r*p/Math.sqrt(f));
        return new Planet(name, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambda, beta)), (float) angularSize, (float) magnitudeF);
    }

    private double[] orbitalCoordinates(double daysSinceJ2010){
        double m = (Angle.TAU/365.242191) * (daysSinceJ2010/t) + lonJ2010 - lonPer;
        double nu = m + 2*e*Math.sin(m);
        double r = (a*(1-e*e)/(1+e*Math.cos(nu)));
        double l = nu + lonPer;
        double[] rl = {r, l};
        return rl;
    }

}
