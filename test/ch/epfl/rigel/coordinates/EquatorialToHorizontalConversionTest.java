package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquatorialToHorizontalConversionTest {



    @Test
    void applyWorksOnKnownValues(){
        //EquatorialToHorizontalConversion e = new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,3,2,17,10,57, 0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5, 45));
        //assertEquals(HorizontalCoordinates.of(0, 0).toString(), e.apply(EquatorialCoordinates.of(Angle.ofHr(2.54), Angle.ofDeg(-23))).toString());
        //EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
        //assertEquals(EquatorialCoordinates.of(Angle.ofHr(9.581478), Angle.ofDMS(19, 32, 6.01)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4,52,31))).toString());
        EquatorialToHorizontalConversion equToHor1= new EquatorialToHorizontalConversion(ZonedDateTime.of(2020,8,14,11,00,00,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        assertEquals(HorizontalCoordinates.ofDeg(65.82367,67.88464).toString(),equToHor1.apply(EquatorialCoordinates.of(Angle.ofHr(8.83764),Angle.ofDeg(49.90546))).toString());
    }
}
