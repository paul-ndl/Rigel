package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;


public abstract class SideralTime {

    public static double greenwich(ZonedDateTime when){
        ZonedDateTime greenwich = when;
        //greenwich = when.
        //return when.withZoneSameInstant(ZoneOffset.UTC);
        return 0;
    }

    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return 0;
    }



}
