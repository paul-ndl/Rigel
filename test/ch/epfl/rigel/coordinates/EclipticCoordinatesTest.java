package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EclipticCoordinatesTest {

    @Test
    void ofFailsWithInvalidCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(Angle.TAU, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(Angle.TAU/2, 0.1+Angle.TAU/4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, -1.58);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(Angle.TAU, -1.58);
        });
    }

    @Test
    void attributesCallWorksOnKnownValues(){
        assertEquals(1.57, EclipticCoordinates.of(1.57, 0).lon());
        assertEquals(0.3421, EclipticCoordinates.of(1.23, 0.3421).lat());
        assertEquals(180, EclipticCoordinates.of(Angle.TAU/2, 0).lonDeg());
        assertEquals(-45, EclipticCoordinates.of(1, -Angle.TAU/8).latDeg());
    }

    @Test
    void toStringWorksOnKnownValues(){
        assertEquals("(λ=60.0000°, β=0.0000°)", EclipticCoordinates.of(Angle.TAU/6, 0).toString());
        assertEquals("(λ=180.0000°, β=46.2100°)", EclipticCoordinates.of(Angle.TAU/2, Angle.ofDeg(46.21)).toString());
        assertEquals("(λ=8.0600°, β=-43.0000°)", EclipticCoordinates.of(Angle.ofDeg(8.06), Angle.ofDeg(-43.00002)).toString());
    }
}
