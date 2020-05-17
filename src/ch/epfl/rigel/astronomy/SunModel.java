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

    private final static double TAU_PER_YEAR = Angle.TAU/365.242191;
    private final static double LON_J2010 = Angle.ofDeg(279.557208);
    private final static double LON_PER = Angle.ofDeg(283.112438);
    private final static double E = 0.016705;
    private final static double SIZE_DENOMINATOR = 1 - E * E;
    private final static double ANGULAR_SIZE_UA = Angle.ofDeg(0.533128);

    /**
     * Retourne le Soleil modélisé pour les arguments donnés
     *
     * @param daysSinceJ2010                 le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion la conversion
     * @return le Soleil modélisé pour les arguments donnés
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion)
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        //Calcul de l'anomalie
        double meanAnomaly = TAU_PER_YEAR*daysSinceJ2010 + LON_J2010 - LON_PER;
        double trueAnomaly = meanAnomaly + 2* E *Math.sin(meanAnomaly);
        //Calcul des coordonnées écliptiques
        double lambda = Angle.normalizePositive(trueAnomaly + LON_PER);
        double beta = 0;
        EclipticCoordinates ec = EclipticCoordinates.of(lambda, beta);
        EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec);
        //Calcul de la taille angulaire
        double angularSize = ANGULAR_SIZE_UA * ((1 + E *Math.cos(trueAnomaly))/ SIZE_DENOMINATOR);
        return new Sun(ec, eq, (float) angularSize, (float) meanAnomaly);
    }
}
