package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToEquatorialConversionTest {

    @Test
    void applyWorksOnKnownValues(){
        //EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2016,7,9,11,31,35, 0, ZoneId.of("Europe/Paris")));
        //assertEquals(EquatorialCoordinates.of(Angle.ofHr(0.83461), Angle.ofDeg(14.05515)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDeg(17), Angle.ofDeg(8))).toString());
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(9.581478), Angle.ofDMS(19, 32, 6.01)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4,52,31))).toString());
        //EclipticToEquatorialConversion a = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        //assertEquals(0, a.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4, 52, 31))).dec());

    }
}
