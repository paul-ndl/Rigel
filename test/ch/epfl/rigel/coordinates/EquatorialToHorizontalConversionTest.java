package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquatorialToHorizontalConversionTest {

    @Test
    void applyWorksOnKnownValues(){
        EquatorialToHorizontalConversion e = new EquatorialToHorizontalConversion(ZonedDateTime.of(2016,7,9,11,31,35, 0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(40, 52));
        assertEquals(HorizontalCoordinates.of(Angle.ofDMS(283, 16, 15.70), Angle.ofDMS(19,20,3.64)).toString(), e.apply(EquatorialCoordinates.of(Angle.ofDeg(17), Angle.ofDMS(23,13, 10))).toString());
        //EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        //assertEquals(EquatorialCoordinates.of(Angle.ofHr(9.581478), Angle.ofDMS(19, 32, 6.01)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4,52,31))).toString());
    }
}
