package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * Un modèle d'une planète
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
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

    /**
     * La liste constituée des huits planètes dans leur ordre de déclaration
     */
    public final static List<PlanetModel> ALL = List.of(values());

    private final static double TAU_PER_YEAR = Angle.TAU / 365.242191;

    private final String name;
    private final double orbitalRev, lonJ2010, lonPer, e, a, i, omega, angularSizeUA, magnitude;

    /**
     * Construit un modèle d'une planète
     *
     * @param name       le nom
     * @param orbitalRev la période de révolution en années tropiques
     * @param lonJ2010   la longitude à J2010 en degrés
     * @param lonPer     la longitude au périgée en degrés
     * @param e          l'excentricité
     * @param a          le demi grand-axe de l'orbite
     * @param i          l'inclinaison de l'orbite à l'écliptique en degrés
     * @param omega      la longitude du noeud ascendant en degrés
     * @param teta0      la taille angulaire en secondes d'arc
     * @param magnitude  la magnitude
     */
    PlanetModel(String name, double orbitalRev, double lonJ2010, double lonPer, double e, double a, double i, double omega, double teta0, double magnitude) {
        this.name = name;
        this.orbitalRev = orbitalRev;
        this.lonJ2010 = Angle.ofDeg(lonJ2010);
        this.lonPer = Angle.ofDeg(lonPer);
        this.e = e;
        this.a = a;
        this.i = Angle.ofDeg(i);
        this.omega = Angle.ofDeg(omega);
        this.angularSizeUA = Angle.ofArcsec(teta0);
        this.magnitude = magnitude;
    }

    /**
     * Retourne la planète modélisée pour les arguments donnés
     *
     * @param daysSinceJ2010                 le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion la conversion
     * @return la planète modélisée pour les arguments donnés
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        //Calcul des coordonnées orbitales
        double[] orbitalCoordinates = orbitalCoordinates(daysSinceJ2010);
        double radius = orbitalCoordinates[0];
        double lon = orbitalCoordinates[1];
        //Calcul des coordonnées orbitales terrestres
        double[] earthOrbitalCoordinates = EARTH.orbitalCoordinates(daysSinceJ2010);
        double earthRadius = earthOrbitalCoordinates[0];
        double earthLon = earthOrbitalCoordinates[1];
        //Calcul des coordonnées écliptiques héliocentriques
        double[] eclipticCoordinates = eclipticCoordinates(radius, lon);
        double projRadius = eclipticCoordinates[0];
        double eclLon = eclipticCoordinates[1];
        double eclLat = eclipticCoordinates[2];
        //Calcul des coordonnées écliptiques géocentriques
        double lambda, beta;
        if (a<1) {
            lambda = Angle.normalizePositive(Math.PI + earthLon + Math.atan2(projRadius * Math.sin(earthLon-eclLon), earthRadius - projRadius*Math.cos(earthLon-eclLon)));
        } else {
            lambda = Angle.normalizePositive(eclLon + Math.atan2(earthRadius * Math.sin(eclLon-earthLon), projRadius - earthRadius*Math.cos(eclLon-earthLon)));
        }
        beta = Math.atan((projRadius * Math.tan(eclLat) * Math.sin(lambda-eclLon)) / (earthRadius * Math.sin(eclLon-earthLon)));
        //Calcul de la taille angulaire et magnitude
        double p = Math.sqrt(earthRadius*earthRadius + radius*radius - 2*earthRadius*radius*Math.cos(lon-earthLon)*Math.cos(eclLat));
        double angularSize = angularSizeUA / p;
        double f = (1 + Math.cos(lambda-lon)) / 2;
        double magnitudeF = magnitude + 5 * Math.log10(radius*p / Math.sqrt(f));

        return new Planet(name, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lambda, beta)), (float) angularSize, (float) magnitudeF);
    }

    /**
     * Retourne le rayon et la longitude orbitale pour
     * le nombre de jours depuis l'époque J2010 donné
     *
     * @param daysSinceJ2010 le nombre de jours depuis l'époque J2010
     * @return le rayon et la longitude orbitale pour
     * le nombre de jours depuis l'époque J2010 donné
     */
    private double[] orbitalCoordinates(double daysSinceJ2010) {
        double meanAnomaly = TAU_PER_YEAR * (daysSinceJ2010 / orbitalRev) + lonJ2010 - lonPer;
        double trueAnomaly = meanAnomaly + 2 * e * Math.sin(meanAnomaly);
        double radius = (a * (1 - e * e)) / (1 + e * Math.cos(trueAnomaly));
        double lon = trueAnomaly + lonPer;
        double[] orbitalCoordinates = {radius, lon};
        return orbitalCoordinates;
    }

    /**
     * Retourne la projection du rayon, la longitude et la latitude écliptiques pour
     * les coordonnées orbitales données
     *
     * @param radius le rayon
     * @param lon    la longitude orbitale
     * @return la projection du rayon, la longitude et la latitude écliptiques pour
     * les coordonnées orbitales données
     */
    private double[] eclipticCoordinates(double radius, double lon) {
        double eclLat = Math.asin(Math.sin(lon - omega) * Math.sin(i));
        double projRadius = radius * Math.cos(eclLat);
        double eclLon = Math.atan2(Math.sin(lon - omega) * Math.cos(i), Math.cos(lon - omega)) + omega;
        double[] eclipticCoordinates = {projRadius, eclLon, eclLat};
        return eclipticCoordinates;
    }

}
