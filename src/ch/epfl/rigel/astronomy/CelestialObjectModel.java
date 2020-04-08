package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Un modèle d'objet céleste
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public interface CelestialObjectModel<O> {

    /**
     * Retourne l'objet modélisé pour le nombre de jours après l'époque J2010
     * en utilisant la conversion donnée pour obtenir ses coordonnées équatoriales
     * à partir de ses coordonnées écliptiques
     *
     * @param daysSinceJ2010                 le nombre de jours depuis l'époque J2010
     * @param eclipticToEquatorialConversion la conversion
     * @return l'objet modélisé pour le nombre de jours après l'époque J2010
     * en utilisant la conversion donnée pour obtenir ses coordonnées équatoriales
     * à partir de ses coordonnées écliptiques
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
