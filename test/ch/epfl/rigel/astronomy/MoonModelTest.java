package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoonModelTest {

    @Test

    void atWorks(){
        EquatorialCoordinates eq1 = MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0,0,0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(14.211456457835897, eq1.raHr());
        assertEquals(-0.20114171346014934, eq1.dec());
    }
}
