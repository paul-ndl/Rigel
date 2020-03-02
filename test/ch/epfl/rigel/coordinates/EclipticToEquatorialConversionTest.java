package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToEquatorialConversionTest {

    @Test
    void applyWorksOnKnownValues(){
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(9.581478), Angle.ofDMS(19, 32, 6.01)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4,52,31))).toString());
        EclipticToEquatorialConversion a = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        assertEquals(0.34095012064184566, a.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4, 52, 31))).dec());
    }
}
