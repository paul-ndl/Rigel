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

    private double t, lonJ2010, lonPer, e, a, i, omega, angularsize, magnitude;

    PlanetModel(String name, double t, double lonJ2010, double lonPer, double e, double a, double i, double omega, double angularsize, double magnitude){
        this.name = name;
        this.t = t;
        this.lonJ2010 = Angle.ofDeg(lonJ2010);
        this.lonPer = Angle.ofDeg(lonPer);
        this.e = e;
        this.a = a;
        this.i = Angle.ofDeg(i);
        this.omega = Angle.ofDeg(omega);
        this.angularsize = angularsize;
        this.magnitude = magnitude;
    }

    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        double [] rlp = rlp(daysSinceJ2010);
        double r = rlp[0];
        double l = rlp[1];
        double psi = rlp[2];
        double rEarth = EARTH.rlp(daysSinceJ2010)[0];
        double lEarth = EARTH.rlp(daysSinceJ2010)[1];
        double lambda, beta;
        if(this.name()=="Mercure" || this.name()=="Vénus"){
            lambda = Math.PI + lEarth + Math.atan2(r*Math.sin(lEarth-l), rEarth-r*Math.cos(lEarth-l));
        } else {
            lambda = l + Math.atan2(rEarth*Math.sin(l-lEarth), r-rEarth*Math.cos(l-lEarth));
        }
        beta = Math.atan2(r*Math.tan(psi)*Math.sin(lambda-l), rEarth*Math.sin(l-lEarth));
        return new Planet(name, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambda, beta)), (float)angularsize, (float)magnitude);
    }

    private double[] rlp (double daysSinceJ2010){
        double d = daysSinceJ2010;
        double m = (Angle.TAU/365.242191) * d/t + lonJ2010 - lonPer;
        double nu = m + 2*e*Math.sin(m);
        double r0 = (a*(1-e*e)/(1+e*Math.cos(nu)));
        double l0 = nu + lonPer;
        double psi = Math.asin(Math.sin(l0-omega)*Math.sin(i));
        double r = r0 * Math.cos(psi);
        double l = Math.atan2(Math.sin(l0-omega)*Math.cos(i), Math.cos(l0-omega)) + omega;
        double[] rlp = {r, l, psi};
        return rlp;
    }

}
