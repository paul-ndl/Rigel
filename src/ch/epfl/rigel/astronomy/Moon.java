package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;

import java.util.Locale;

/**
 * La Lune
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Moon extends CelestialObject {

    private final static Interval PHASE_INTERVAL = ClosedInterval.of(0, 1);

    private final float phase;

    /**
     * Construit la Lune
     *
     * @param equatorialPos la position équatoriale
     * @param angularSize   la taille angulaire
     * @param magnitude     la magnitude
     * @param phase         la phase
     * @throws IllegalArgumentException si la phase n'est pas comprise entre 0 et 1
     * @see CelestialObject#CelestialObject(String, EquatorialCoordinates, float, float)
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        Preconditions.checkInInterval(PHASE_INTERVAL, phase);
        this.phase = phase;
    }

    /**
     * Retourne un texte informatif sur la Lune
     *
     * @return son nom et sa phase en pourcentage
     */
    @Override
    public String info() {
        return String.format(Locale.ROOT,
                      "%s (%.1f%%)",
                      super.name(),
                             phase * 100);
    }
}
