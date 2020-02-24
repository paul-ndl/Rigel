package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EquatorialCoordinatesTest {

    @Test
    void ofFailsWithInvalidCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.TAU, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.TAU/2, 0.1+Angle.TAU/4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, -1.58);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Angle.TAU, -1.58);
        });
    }

    @Test
    void attributesCallWorksOnKnownValues(){
        assertEquals(1.57, EquatorialCoordinates.of(1.57, 0).ra());
        assertEquals(0.3421, EquatorialCoordinates.of(1.23, 0.3421).dec());
        assertEquals(180, EquatorialCoordinates.of(Angle.TAU/2, 0).raDeg());
        assertEquals(-45, EquatorialCoordinates.of(1, -Angle.TAU/8).decDeg());
        assertEquals(12, EquatorialCoordinates.of(Angle.TAU/2, -0.42).raHr(), 1e-9);
        assertEquals(6, EquatorialCoordinates.of(Angle.TAU/4, -0.42).raHr(), 1e-9);
        assertEquals(4, EquatorialCoordinates.of(Angle.TAU/6, 0.103).raHr(), 1e-9);
    }

    @Test
    void toStringWorksOnKnownValues(){
        assertEquals("(ra=12.0000h, dec=22.5000°)", EquatorialCoordinates.of(Angle.TAU/2, Angle.TAU/16).toString());
        assertEquals("(ra=6.0000h, dec=-45.0000°)", EquatorialCoordinates.of(Angle.TAU/4, -Angle.TAU/8).toString());
    }


}
