package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AsterismTest {
    @Test
    void isConstructorValidWithValidValues(){
        Star s= new Star(9, "lol", EquatorialCoordinates.of(0,0),0f,0.5f);
        List<Star> stars= new ArrayList<>();
        stars.add(s);
        List<Star> stars1= new ArrayList<>();
        stars1.add(s);
        Asterism a = new Asterism(stars);
        assertEquals(stars1,a.stars()); }


    @Test
    void isConstructorValidWithInValidValues(){
        List<Star> stars= new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Asterism(stars);
        });
    }
}
