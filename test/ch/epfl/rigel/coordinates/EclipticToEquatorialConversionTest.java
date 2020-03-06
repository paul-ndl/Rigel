package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToEquatorialConversionTest {

    @Test
    void applyOK1() {
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2020, 8, 14, 11, 0, 0, 0, ZoneId.of("Europe/Paris")));
        EquatorialToHorizontalConversion f = new EquatorialToHorizontalConversion(ZonedDateTime.of(2020, 8, 14, 11, 0, 0, 0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5, 45));
        //EclipticCoordinates ec1 = EclipticCoordinates.of(Math.PI, Math.PI / 6);
        //assertEquals(HorizontalCoordinates.ofDeg(68.71179, 17.29562).toString(), f.apply(e.apply(ec1)).toString());
    }

    @Test
    void applyWorksOnKnownValues() {
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009, 7, 6, 0, 0, 0, 0, ZoneId.of("UTC")));
        assertEquals(EquatorialCoordinates.of(Angle.ofHr(9.581478), Angle.ofDMS(19, 32, 6.01)).toString(), e.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4, 52, 31))).toString());
        EclipticToEquatorialConversion a = new EclipticToEquatorialConversion(ZonedDateTime.of(2009, 7, 6, 0, 0, 0, 0, ZoneId.of("UTC")));
        assertEquals(0.34095012064184566, a.apply(EclipticCoordinates.of(Angle.ofDMS(139, 41, 10), Angle.ofDMS(4, 52, 31))).dec());
    }
    @Test
        void applyOK(){
            EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("UTC")));
            assertEquals(EquatorialCoordinates.of(Angle.ofDeg(143.7225092),Angle.ofDeg(19.53569924)).raDeg(), e.apply(EclipticCoordinates.of(Angle.ofDeg(139.6861111), Angle.ofDeg(4.875277778))).raDeg(), 1e-3);
            //EclipticToEquatorialConversion f = new EclipticToEquatorialConversion(ZonedDateTime.of(2020,8,14,11,0,0, 0, ZoneId.of("UTC")));
            //assertEquals(EquatorialCoordinates.of(Angle.ofHr(11.8583), Angle.ofDeg(58.6403)).toString(), f.apply(EclipticCoordinates.of(Angle.ofDMS(120, 30, 10), Angle.ofDMS(30,52,31))).toString());
        }
    }

