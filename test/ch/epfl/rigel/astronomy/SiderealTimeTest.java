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
        assertEquals(Angle.ofHr(4.668119),SiderealTime.greenwich(ZonedDateTime.of(1980,4,22,14,36,51, (int) 67e7, ZoneId.of("UTC"))), 10e-7);

    }

    @Test
    void localWorksWithKnownValues(){
        assertEquals(Angle.ofHr(0.401453),SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51, (int) 67e7, ZoneOffset.UTC), GeographicCoordinates.ofDeg(-64,45)), 10e-7);

    }
}
