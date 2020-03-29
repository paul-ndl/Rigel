package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Le Soleil
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Sun extends CelestialObject {

    private static final float MAGNITUDE = (float)-26.7;
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Construit la Lune
     * @param equatorialPos
     *          la position équatoriale
     * @param eclipticPos
     *          la position écliptique
     * @param angularSize
     *          la taille angulaire
     * @param meanAnomaly
     *          l'anomalie moyenne
     * @throws NullPointerException
     *          si la position écliptique est nulle
     * @see CelestialObject#CelestialObject(String, EquatorialCoordinates, float, float)
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly){
        super("Soleil", equatorialPos, angularSize, MAGNITUDE);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Retourne la position écliptique
     * @return la position écliptique
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * Retourne l'anomalie moyenne
     * @return l'anomalie moyenne
     */
    public double meanAnomaly(){
        return  meanAnomaly;
    }
}
