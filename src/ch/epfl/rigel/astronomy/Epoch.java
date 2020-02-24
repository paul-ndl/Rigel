package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.MIDNIGHT, ZoneOffset.UTC));

    private ZonedDateTime epoch;

    private Epoch(ZonedDateTime epoch){
        this.epoch = epoch;
    }

    public ZonedDateTime getZonedDateTime(){
        return this.epoch;
    }

    public double daysUntil(ZonedDateTime when){
        return (double) getZonedDateTime().until(when, ChronoUnit.MILLIS)/86400000;
    }

    public double julianCenturiesUntil(ZonedDateTime when){
        return (double) getZonedDateTime().until(when, ChronoUnit.MILLIS)/3.15576e12;
    }


}
