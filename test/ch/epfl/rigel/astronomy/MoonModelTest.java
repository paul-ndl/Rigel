package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoonModelTest {

    @Test

    void atWorks(){
        EquatorialCoordinates eq1 = MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0,0,0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(14.211456457836277, eq1.raHr(), 1e-12);
        assertEquals(-0.20114171346014934, eq1.dec());
        double angularSize = MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0), ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).angularSize();
        assertEquals(0.009225908666849136,angularSize);
        String info = MoonModel.MOON.at(J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0), ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1), LocalTime.of(0, 0),ZoneOffset.UTC))).info();
        assertEquals("Lune (22.5%)", info);
    }
}
