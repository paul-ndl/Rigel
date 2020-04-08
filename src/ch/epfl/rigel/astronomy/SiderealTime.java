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
        ZonedDateTime whenGreenwichStartDay = when.withZoneSameInstant(ZoneOffset.UTC)
                                                  .truncatedTo(ChronoUnit.DAYS);
        double centuries = Epoch.J2000.julianCenturiesUntil(whenGreenwichStartDay);
        double hours = (double) whenGreenwichStartDay.until(when, ChronoUnit.MILLIS) / MILLIS_IN_HOUR;
        Polynomial polynomial = Polynomial.of(0.000025862,
                                             2400.051336,
                                                          6.697374558);
        double sA = polynomial.at(centuries);
        double sB = 1.002737909 * hours;
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
