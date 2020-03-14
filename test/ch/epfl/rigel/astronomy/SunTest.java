package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SunTest {

    @Test
    void fails(){
        assertThrows(NullPointerException.class, () -> {
            new Sun(null, EquatorialCoordinates.of(0,0), 1f, -17f);
        });
        assertThrows(NullPointerException.class, () -> {
            new Sun(EclipticCoordinates.of(0,0), null, 1f, -17f);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0), -0.1f, -17f);
        });
    }

    @Test
    void nameTesT(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals("Soleil", p.name());
    }

    @Test
    void angularSizeTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals(1, p.angularSize());
    }

    @Test
    void magnitudeTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals(-26.7, p.magnitude(), 1e-6);
    }

    @Test
    void equatorialPosTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals(EquatorialCoordinates.of(0,0).toString(), p.equatorialPos().toString());
    }

    @Test
    void eclipticPosTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals(EclipticCoordinates.of(0,0).toString(), p.eclipticPos().toString());
    }

    @Test
    void infoTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals("Soleil", p.info());
    }


    @Test
    void meanAnomalyTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals(-22, p.meanAnomaly());
    }

    @Test
    void toStringTest(){
        Sun p = new Sun(EclipticCoordinates.of(0,0), EquatorialCoordinates.of(0,0),1,-22f);
        assertEquals("Soleil", p.toString());
    }

}
