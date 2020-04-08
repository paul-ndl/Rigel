package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Un temps sidéral
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class SiderealTime {

    private final static double MILLIS_IN_HOUR = 3600000;

    /**
     * Retourne le temps sidéral de Greenwich en radians pour le couple heure/date donné
     *
     * @param when le couple heure/date
     */
    public static double greenwich(ZonedDateTime when) {
        final ZonedDateTime whenGreenwichStartDay = when.withZoneSameInstant(ZoneOffset.UTC)
                                                        .truncatedTo(ChronoUnit.DAYS);
        final double centuries = Epoch.J2000.julianCenturiesUntil(whenGreenwichStartDay);
        final double hours = (double) whenGreenwichStartDay.until(when, ChronoUnit.MILLIS) / MILLIS_IN_HOUR;
        final Polynomial polynomial = Polynomial.of(0.000025862,
                                          2400.051336,
                                                       6.697374558);
        final double sA = polynomial.at(centuries);
        final double sB = 1.002737909 * hours;
        return Angle.normalizePositive(Angle.ofHr(sA + sB));
    }

    /**
     * Retourne le temps sidéral local en radians pour le couple heure/date donné
     *
     * @param when le couple heure/date
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}
