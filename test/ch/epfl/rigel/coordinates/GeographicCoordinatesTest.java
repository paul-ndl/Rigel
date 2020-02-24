package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeographicCoordinatesTest {

    @Test
    void ofDegFailsWithInvalidCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(256, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(-182, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(0, 90.002);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(0, -103);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GeographicCoordinates.ofDeg(400, -103);
        });
    }

    @Test
    void attributesCallWorksOnKnownValues(){
        assertEquals(-Angle.TAU/6, GeographicCoordinates.ofDeg(-60, -22.5).lon());
        assertEquals(179.2, GeographicCoordinates.ofDeg(179.2, 12).lonDeg());
        assertEquals(-Angle.TAU/16, GeographicCoordinates.ofDeg(53.987, -22.5).lat());
        assertEquals(-22.5, GeographicCoordinates.ofDeg(-34, -22.5).latDeg());
    }

    @Test
    void toStringWorksOnKnownValues(){
        assertEquals("(lon=-130.0000°, lat=7.2000°)", GeographicCoordinates.ofDeg(-130, 7.2).toString());
        assertEquals("(lon=4.1112°, lat=80.8000°)", GeographicCoordinates.ofDeg(4.11122, 80.8).toString());
        assertEquals("(lon=154.0900°, lat=-9.5400°)", GeographicCoordinates.ofDeg(154.09, -9.54).toString());
    }


}
