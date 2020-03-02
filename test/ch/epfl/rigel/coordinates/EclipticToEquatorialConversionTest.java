package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToEquatorialConversionTest {

    @Test
    void applyWorksOnKnownValues(){
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2016,7,9,11,31,35, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(0.73106), Angle.ofDeg(11.03858)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDeg(14.4031), Angle.ofDeg(5.82))).toString());
    }
}
