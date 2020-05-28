package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Un bean contenant l'instant d'observation
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class DateTimeBean {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ZoneId> zoneId = new SimpleObjectProperty<>(null);

    /**
     * Retourne la propriété de la date d'observation
     *
     * @return la propriété de la date d'observation
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    private LocalDate getDate() {
        return date.get();
    }

    private void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * Retourne la propriété du temps d'observation
     *
     * @return la propriété du temps d'observation
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    private LocalTime getTime() {
        return time.get();
    }

    private void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     * Retourne la propriété de la zone d'observation
     *
     * @return la propriété de la zone d'observation
     */
    public ObjectProperty<ZoneId> zoneIdProperty() {
        return zoneId;
    }

    private ZoneId getZoneId() {
        return zoneId.get();
    }

    private void setZoneId(ZoneId zoneId) {
        this.zoneId.set(zoneId);
    }

    /**
     * Retourne l'instant d'observation complet
     *
     * @return l'instant d'observation complet
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(getDate(), getTime(), getZoneId());
    }

    /**
     * Paramètre l'instant d'observation à celui donné
     *
     * @param zonedDateTime le nouvel instant d'observation
     */
    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        setDate(zonedDateTime.toLocalDate());
        setTime(zonedDateTime.toLocalTime());
        setZoneId(zonedDateTime.getZone());
    }


}
