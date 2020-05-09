package ch.epfl.rigel.bonus;

import java.time.ZoneId;

public final class City {

    private String name;
    private double lon;
    private double lat;
    private ZoneId zone;

    public City(String name, double lon, double lat, String zone){
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.zone = ZoneId.of(zone);
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
