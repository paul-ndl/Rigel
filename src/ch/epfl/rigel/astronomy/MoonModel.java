package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Un modèle de la Lune
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {
    MOON;

    private static final double averageLon = Angle.ofDeg(91.929336);
    private static final double lonPer = Angle.ofDeg(130.143076);
    private static final double lonNode = Angle.ofDeg(291.682547);
    private static final double i = Angle.ofDeg(5.145396);
    private static final double e = 0.0549;
    private static final double angularSizeUA = Angle.ofDeg(0.5181);

    /**
     * Retourne la Lune modélisée pour les arguments donnés
     *
     * @param daysSinceJ2010                 le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion la conversion
     * @return la Lune modélisée pour les arguments donnés
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        //Données du Soleil
        double lambdaSun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).eclipticPos().lon();
        double meanAnomalySun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).meanAnomaly();
        //Calcul de la longitude orbitale
        double orbitalLon = Angle.ofDeg(13.1763966)*daysSinceJ2010 + averageLon;
        double meanAnomaly = orbitalLon - Angle.ofDeg(0.1114041)*daysSinceJ2010 - lonPer;
        double evection = Angle.ofDeg(1.2739) * Math.sin(2*(orbitalLon-lambdaSun) - meanAnomaly);
        double annualEquation = Angle.ofDeg(0.1858) * Math.sin(meanAnomalySun);
        double a3 = Angle.ofDeg(0.37) * Math.sin(meanAnomalySun);
        double correctedAnomaly = meanAnomaly + evection - annualEquation - a3;
        double centerEquation = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double a4 = Angle.ofDeg(0.214) * Math.sin(2*correctedAnomaly);
        double correctedLon = orbitalLon + evection + centerEquation - annualEquation + a4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2*(correctedLon-lambdaSun));
        double trueLon = correctedLon + variation;
        //Calcul de la longitude du noeud ascendant
        double averageLonNode = lonNode - Angle.ofDeg(0.0529539)*daysSinceJ2010;
        double correctedLonNode = averageLonNode - Angle.ofDeg(0.16)*Math.sin(meanAnomalySun);
        //Calcul des coordonnées écliptiques
        double lambda = Angle.normalizePositive(Math.atan2(Math.sin(trueLon-correctedLonNode) * Math.cos(i), Math.cos(trueLon-correctedLonNode)) + correctedLonNode);
        double beta = Math.asin(Math.sin(trueLon - correctedLonNode) * Math.sin(i));
        EclipticCoordinates ecl = EclipticCoordinates.of(lambda, beta);
        //Calcul de la phase et de la taille angulaire
        double phase = (1 - Math.cos(trueLon-lambdaSun)) / 2;
        double p = (1 - e*e) / (1 + e*Math.cos(correctedAnomaly+centerEquation));
        double angularSize = angularSizeUA / p;
        return new Moon(eclipticToEquatorialConversion.apply(ecl), (float) angularSize, 0f, (float) phase);
    }


}
