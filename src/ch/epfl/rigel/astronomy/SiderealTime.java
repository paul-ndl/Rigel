package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.time.temporal.ChronoUnit;

public final class SiderealTime {

    /**
     * returns an approximation of the greenwich sideral time of the given date
     */
    public static double greenwich(ZonedDateTime when){
        ZonedDateTime whenGreenwichStartDay = when.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);
        double centuries = Epoch.J2000.julianCenturiesUntil(whenGreenwichStartDay);
        double hours = (double) whenGreenwichStartDay.until(when, ChronoUnit.MILLIS)/3600000;
        Polynomial p = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
        double sA = p.at(centuries);
        double sB = 1.002737909 * hours;
        return Angle.normalizePositive(Angle.ofHr(sA + sB));
    }

    /**
     * returns an approximation of the local sideral time of the given date
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}
