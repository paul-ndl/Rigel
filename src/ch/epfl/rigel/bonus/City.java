package ch.epfl.rigel.bonus;

import java.time.ZoneId;
import java.time.ZoneOffset;

public final class City {

    private String name;
    private double lon;
    private double lat;
    private ZoneId zone;

    public City(String name, double lon, double lat, double zone){
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        int hours = (int) zone;
        //int mins = zone%1 == 0 ? 0 : 30;
        this.zone = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(hours));
    }

    public String getName(){
        return name;
    }

    public double getLonDeg(){
        return lon;
    }

    public double getLatDeg(){
        return lat;
    }

    public ZoneId getZoneId(){
        return zone;
    }

    @Override
    public String toString(){
        return name;
    }



}
