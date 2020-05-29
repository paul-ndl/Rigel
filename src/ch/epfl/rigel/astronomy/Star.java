package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;

/**
 * Une étoile
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Star extends CelestialObject {

    private final static Interval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    private final int hipparcosId;
    private final int colorTemperature;

    /**
     * Construit une étoile
     *
     * @param hipparcosId   le numéro Hipparcos
     * @param name          le nom
     * @param equatorialPos la position équatoriale
     * @param magnitude     la magnitude
     * @param colorIndex    l'indice de couleur
     * @throws IllegalArgumentException si le numéro Hipparcos est négatif
     *                                  si l'indice de couleur n'est pas compris entre -0.5 et 5.5
     * @see CelestialObject#CelestialObject(String, EquatorialCoordinates, float, float)
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0f, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);
        Preconditions.checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        this.hipparcosId = hipparcosId;
        double colorIndexMult = 0.92*colorIndex;
        colorTemperature = (int) (4600 * (1d/(colorIndexMult + 1.7) + 1d/(colorIndexMult + 0.62)));
    }

    /**
     * Retourne le numéro Hipparcos
     *
     * @return le numéro Hipparcos
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Retourne la température de couleur en degrés Kelvin
     *
     * @return la température de couleur en degrés Kelvin
     */
    public int colorTemperature() {
        return colorTemperature;
    }
}
