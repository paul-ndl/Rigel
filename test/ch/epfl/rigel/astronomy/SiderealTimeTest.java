package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SiderealTimeTest {

    @Test
    void greenwichWorksWithKnownValues(){
        //ZonedDateTime a = ZonedDateTime.of();
        assertEquals(Angle.ofHr(4.668119),SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51,(int) 67e7, ZoneId.of("UTC"))));

    }

    @Test
    void localWorksWithKnownValues(){
        //ZonedDateTime a = ZonedDateTime.of();
        //assertEquals(1.74570958832716,SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,67, ZoneOffset.UTC), GeographicCoordinates.ofDeg(30,45)), 10e-10);

    }
}
