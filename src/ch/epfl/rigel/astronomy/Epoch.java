package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Une époque
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum Epoch {

    /**
     * L'époque J2000 (1er janvier 2000 à 12h00 UTC)
     */
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneOffset.UTC)),
    /**
     * L'époque J2010 (31 décembre 2009 à 0h00 UTC)
     */
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.MIDNIGHT, ZoneOffset.UTC));

    private final ZonedDateTime epoch;

    /**
     * Construit une époque à partir d'un couple heure/date donné
     * @param epoch
     *          le couple heure/date
     */
    Epoch(ZonedDateTime epoch){
        this.epoch = epoch;
    }

    /**
     * Retourne le nombre de jours entre l'époque appliquée et l'instant donné
     * @param when
     *          l'instant
     * @return le nombre de jours entre l'époque appliquée et l'instant donné
     */
    public double daysUntil(ZonedDateTime when){
        return (double) epoch.until(when, ChronoUnit.MILLIS)/86400000;
    }

    /**
     * Retourne le nombre de siècles juliens entre l'époque appliquée et l'instant donné
     * @param when
     *          l'instant
     * @return le nombre de siècles juliens entre l'époque appliquée et l'instant donné
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return (double) epoch.until(when, ChronoUnit.MILLIS)/3.15576e12;
    }

}
