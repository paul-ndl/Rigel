package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlanetTest {

    @Test
    void fails(){
        assertThrows(NullPointerException.class, () -> {
            new Planet(null, EquatorialCoordinates.of(0,0), 1f, -17f);
        });
        assertThrows(NullPointerException.class, () -> {
            new Planet("Saturne", null, 1f, -17f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Planet("Uranus", EquatorialCoordinates.of(0,0), 0f, -17f);
        });
    }

    @Test
    void nameTesT(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals("venus", p.name());
    }

    @Test
    void angularSizeTest(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals(1, p.angularSize());
    }

    @Test
    void magnitudeTest(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals(-22, p.magnitude());
    }

    @Test
    void equatorialPosTest(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals(EquatorialCoordinates.of(0,0).toString(), p.equatorialPos().toString());
    }

    @Test
    void infoTest(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals("venus", p.info());
    }

    @Test
    void toStringTest(){
        Planet p = new Planet("venus", EquatorialCoordinates.of(0,0),1,-22);
        assertEquals("venus", p.toString());
        }


}
