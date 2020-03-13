package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CartesianCoordinatesTest {

    @Test
    public void isConstructorValid(){
        CartesianCoordinates c1= CartesianCoordinates.of(12.5,45.6);
        assertEquals(12.5,c1.x());
        assertEquals(45.6,c1.y());
        CartesianCoordinates c2 = CartesianCoordinates.of(56.9876372949,87.98299479739874);
        assertEquals(56.9876372949,c2.x());
        assertEquals(87.98299479739874,c2.y()); }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates coordonnées= CartesianCoordinates.of(9,0);
            coordonnées.equals(coordonnées);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates coordinates = CartesianCoordinates.of(0, 75);
            coordinates.hashCode();
        });
    }
}

