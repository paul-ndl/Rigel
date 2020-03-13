package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    /**
     * initializes the epoch 2000 (1st january 2000 12:00 UTC)
     */
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneOffset.UTC)),
    /**
     * initializes the epoch 2010 (0 january 2010 00:00 UTC)
     */
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.MIDNIGHT, ZoneOffset.UTC));

    private ZonedDateTime epoch;

    /**
     * constructor for the epoch
     */
    Epoch(ZonedDateTime epoch){
        this.epoch = epoch;
    }

    /**
     * returns the number of days between an epoch and the given date
     */
    public double daysUntil(ZonedDateTime when){
        return (double) this.epoch.until(when, ChronoUnit.MILLIS)/86400000;
    }

    /**
     * returns the number of julian centuries between an epoch and the given date
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return (double) this.epoch.until(when, ChronoUnit.MILLIS)/3.15576e12;
    }


}
