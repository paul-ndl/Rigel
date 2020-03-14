package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StarTest {

    @Test

    void colorTemperaturWorksOnKnownValues(){
        Star s1 = new Star(24436, "Rigel", EquatorialCoordinates.of(0, 0), 0, -0.03f);
        assertEquals(10515, s1.colorTemperature());
        Star s2 = new Star(27989, "Betelgeuse", EquatorialCoordinates.of(0, 0), 0, 1.50f);
        assertEquals(3793, s2.colorTemperature());
    }
}
