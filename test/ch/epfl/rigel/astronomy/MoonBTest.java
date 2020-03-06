package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoonBTest {

    MoonB m = new MoonB(EquatorialCoordinates.of(Angle.TAU/4, 0), (float)0.5, (float) -12.9, (float)0.3752);
    MoonB m1 = new MoonB(EquatorialCoordinates.of(Angle.TAU/4, 0), (float)0.5, (float) -12.9, (float)-1);
    SunB s = new SunB(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(Angle.TAU/4, 0), (float)0.5, (float) -12.9);

    @Test
    void fails(){
        assertThrows(IllegalArgumentException.class, () -> {
            new SunB(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(Angle.TAU/4, 0), (float)-0.5, (float) -12.9);
        });
    }

    @Test
    void ofDMSFailsWithInvalidMinutes() {
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(3, -1, 2.4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(1, -5, 7);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(122, -12, 4.5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(33, 60, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(9, 65, 14);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Angle.ofDMS(0, 199, 0);
        });
    }

    @Test
    void infoWorks(){
        assertEquals("Lune (37.5%)",m.toString());
    }
}
