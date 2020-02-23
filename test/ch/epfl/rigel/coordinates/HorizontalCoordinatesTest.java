package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HorizontalCoordinatesTest {

    @Test
    void ofFailsWithInvalidCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(Angle.TAU, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(Angle.TAU/2, 0.1+Angle.TAU/4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(0, -1.58);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.of(Angle.TAU, -1.58);
        });
    }

    @Test
    void ofDegFailsWithInvalidCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(360, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, 90.002);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(0, -103);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(400, -103);
        });
    }

    @Test
    void azOctantNameWorksOnKnownValues(){
        assertEquals("NO", HorizontalCoordinates.ofDeg(335, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("N", HorizontalCoordinates.ofDeg(338, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("N", HorizontalCoordinates.ofDeg(22.467, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("NE", HorizontalCoordinates.ofDeg(22.5, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("NE", HorizontalCoordinates.ofDeg(67, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("SE", HorizontalCoordinates.ofDeg(138, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("S", HorizontalCoordinates.ofDeg(173, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("SO", HorizontalCoordinates.ofDeg(223.66789, 0).azOctantName("N", "E", "S", "O"));
        assertEquals("O", HorizontalCoordinates.ofDeg(284, 0).azOctantName("N", "E", "S", "O"));
    }

    @Test
    void attributesCallWorksOnKnownValues(){
        assertEquals(1.57, HorizontalCoordinates.of(1.57, 0).az());
        assertEquals(0.3421, HorizontalCoordinates.of(1.23, 0.3421).alt());
        assertEquals(180, HorizontalCoordinates.of(Angle.TAU/2, 0).azDeg());
        assertEquals(-45, HorizontalCoordinates.of(Angle.TAU/2, -Angle.TAU/8).altDeg());
        assertEquals(180, HorizontalCoordinates.ofDeg(180, 12).azDeg());
        assertEquals(-Angle.TAU/16, HorizontalCoordinates.ofDeg(324, -22.5).alt());
    }

    @Test
    void angularDistanceToWorksOnKnownValues(){
        assertEquals(1.3505626656387095, HorizontalCoordinates.of(1.57, 0.256).angularDistanceTo(HorizontalCoordinates.of(0.13, 0.42)));
        assertEquals(2.4183326952843602, HorizontalCoordinates.ofDeg(49.3, -56).angularDistanceTo(HorizontalCoordinates.ofDeg(6.17, 80.43)));
        assertEquals(0.027935461189288496, HorizontalCoordinates.ofDeg(6.5682, 46.5183).angularDistanceTo(HorizontalCoordinates.ofDeg(8.5476, 47.3763)));
        assertEquals(0.027935461189288496, HorizontalCoordinates.ofDeg(8.5476, 47.3763).angularDistanceTo(HorizontalCoordinates.ofDeg(6.5682, 46.5183)));
    }

    @Test
    void toStringWorksOnKnownValues(){
        assertEquals("(az=350.0000°, alt=7.2000°)", HorizontalCoordinates.ofDeg(350, 7.2).toString());
        assertEquals("(az=4.1112°, alt=80.8000°)", HorizontalCoordinates.ofDeg(4.11122, 80.8).toString());
        assertEquals("(az=254.0900°, alt=-9.5400°)", HorizontalCoordinates.ofDeg(254.09, -9.54).toString());
    }

}
