package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeBean {

    private ObjectProperty<LocalDate> date = null;
    private ObjectProperty<LocalTime> time = null;
    private ObjectProperty<ZoneId> zoneId = null;

    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    public LocalDate getDate(){
        return date.get();
    }

    public void setDate(LocalDate date){
        this.date.set(date);
    }

    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    public LocalTime getTime(){
        return time.get();
    }

    public void setTime(LocalTime time){
        this.time.set(time);
    }

    public ObjectProperty<ZoneId> zoneIdProperty(){
        return zoneId;
    }

    public ZoneId getZoneId(){
        return zoneId.get();
    }

    public void setZoneId(ZoneId zoneId){
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
