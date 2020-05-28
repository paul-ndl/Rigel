package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Un accélérateur de temps
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Retourne le temps simulé à partir du temps initial et du temps réel écoulé
     *
     * @param initTime        le temps initial
     * @param elapsedRealTime le temps réel écoulé
     * @return le temps simulé à partir du temps initial et du temps réel écoulé
     */
    ZonedDateTime adjust(ZonedDateTime initTime, long elapsedRealTime);

    /**
     * Retourne un accélérateur de temps continu en fonction du facteur d'accélération
     *
     * @param alpha le facteur d'accélération
     * @return un accélérateur de temps continu en fonction du facteur d'accélération
     */
    static TimeAccelerator continuous(int alpha) {
        return (ZonedDateTime initTime, long elapsedRealTime) ->
                initTime.plusNanos(alpha * elapsedRealTime);
    }

    /**
     * Retourne un accélérateur de temps discret en fonction de la fréquence d'avancement
     * et du pas
     *
     * @param f la fréquence d'avancement
     * @param S le pas
     * @return un accélérateur de temps discret en fonction de la fréquence d'avancement
     * et du pas
     */
    static TimeAccelerator discrete(int f, Duration S) {
        return (ZonedDateTime initTime, long elapsedRealTime) ->
                initTime.plusNanos((long) Math.floor(f * elapsedRealTime * 1e-9) * S.toNanos());
    }

}
