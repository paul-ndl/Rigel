package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SiderealTimeTest {

    @Test
    void greenwichWorksWithKnownValues(){
        //ZonedDateTime a = ZonedDateTime.of();
        assertEquals(1.2220619247737088,SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51,67, ZoneId.of("UTC"))), 10e-10);

    }

    @Test
    void localWorksWithKnownValues(){
        //ZonedDateTime a = ZonedDateTime.of();
        assertEquals(1.74570958832716,SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,67, ZoneOffset.UTC), GeographicCoordinates.ofDeg(30,45)), 10e-10);

    }
}
