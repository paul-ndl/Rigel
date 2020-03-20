package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            new Moon(EquatorialCoordinates.of(0,0), -0.5f, -17f, 2f);
        });
    }


    @Test
    void nameTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.5f);
        assertEquals("Lune", p.name());
    }

    @Test
    void angularSizeTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.5f);
        assertEquals(1, p.angularSize());
    }

    @Test
    void magnitudeTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.5f);
        assertEquals(2, p.magnitude());
    }

    @Test
    void equatorialPosTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.5f);
        assertEquals(EquatorialCoordinates.of(0,0).toString(), p.equatorialPos().toString());
    }

    @Test
    void infoTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.3752f);
        assertEquals("Lune (37.5%)", p.info());
    }

    @Test
    void toStringTest(){
        Moon p = new Moon(EquatorialCoordinates.of(0,0), 1,2,0.5f);
        assertEquals("Lune (50.0%)", p.toString());
    }
}
