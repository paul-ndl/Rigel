package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetModelTest {

    @Test
    void atWorksOnKnownValues(){
        EquatorialCoordinates eq1 = PlanetModel.JUPITER.at(-2231.0, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(11.18675858733765, eq1.raHr(), 1e-12);
        assertEquals(6.3426361553333095, eq1.decDeg(), 1e-12);

        EquatorialCoordinates eq2 = PlanetModel.MERCURY.at(-2231.0, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos();
        assertEquals(16.8279572540347, eq2.raHr(), 1e-12);
        assertEquals(-23.612022868326743, eq2.decDeg(), 1e-12);

        Planet p = PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)));
        assertEquals(35.11141185362771, Angle.toDeg(p.angularSize())*3600);
        assertEquals(-1.9885659217834473 , p.magnitude());
    }
}
