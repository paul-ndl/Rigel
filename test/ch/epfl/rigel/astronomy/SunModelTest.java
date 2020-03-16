package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SunModelTest {

    @Test
    void atWorksOnKnownValues(){
        EquatorialCoordinates eq1 = SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(8.392682808297808, eq1.raHr());
        assertEquals(19.35288373097352, eq1.decDeg());

        EquatorialCoordinates eq2 = SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(5.9325494700300885, eq2.ra());

        Sun s = SunModel.SUN.at(-7494, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(1988, Month.JULY, 27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)));
        //assertEquals(Angle.ofDMS(0,31,30), Angle.ofDeg(s.angularSize()));
    }
}
