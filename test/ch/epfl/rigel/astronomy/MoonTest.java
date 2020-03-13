package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoonTest {

    @Test
    void fails(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(EquatorialCoordinates.of(0,0), 1f, -17f, -0.5f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(EquatorialCoordinates.of(0,0), -0.5f, -17f, 0.5f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(EquatorialCoordinates.of(0,0), 1f, -17f, -0.5f);
        });
    }
}
