package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * Une étoile
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final float colorIndex;

    /**
     * Construit une étoile
     * @param hipparcosId
     *          le numéro Hipparcos
     * @param name
     *          le nom
     * @param equatorialPos
     *          la position équatoriale
     * @param magnitude
     *          la magnitude
     * @param colorIndex
     *          l'indice de couleur
     * @throws IllegalArgumentException
     *          si le numéro Hipparcos est négatif
     *          si l'indice de couleur n'est pas compris entre -0.5 et 5.5
     * @see CelestialObject#CelestialObject(String, EquatorialCoordinates, float, float)
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos, 0f, magnitude);
        Preconditions.checkArgument(hipparcosId>=0);
        Preconditions.checkInInterval(ClosedInterval.of(-0.5,5.5), colorIndex);
        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    /**
     * Retourne le numéro Hipparcos
     * @return le numéro Hipparcos
     */
    public int hipparcosId(){
        return hipparcosId;
    }

    /**
     * Retourne la température de couleur en degrés Kelvin
     * @return la température de couleur en degrés Kelvin
     */
    public int colorTemperature(){
        return (int) Math.floor(4600*(1/(0.92*colorIndex+1.7) + 1/(0.92*colorIndex+0.62)));
    }
}
