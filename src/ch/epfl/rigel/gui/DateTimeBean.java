package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeBean {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ZoneId> zoneId = new SimpleObjectProperty<>(null);

    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    private LocalDate getDate(){
        return date.get();
    }

    private void setDate(LocalDate date){
        this.date.set(date);
    }

    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    private LocalTime getTime(){
        return time.get();
    }

    private void setTime(LocalTime time){
        this.time.set(time);
    }

    public ObjectProperty<ZoneId> zoneIdProperty(){
        return zoneId;
    }

    private ZoneId getZoneId(){
        return zoneId.get();
    }

    private void setZoneId(ZoneId zoneId){
        this.zoneId.set(zoneId);
    }

    public ZonedDateTime getZonedDateTime(){
        return ZonedDateTime.of(getDate(), getTime(), getZoneId());
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime){
        setDate(zonedDateTime.toLocalDate());
        setTime(zonedDateTime.toLocalTime());
        setZoneId(zonedDateTime.getZone());
    }


}
