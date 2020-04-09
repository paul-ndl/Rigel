package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static ch.epfl.rigel.gui.BlackBodyColor.colorForTemperature;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlackBodyColorTest {

    @Test
    void colorForTemperatureWorks() {
        assertEquals(Color.rgb(0xc7, 0xd8, 0xff), colorForTemperature(10634));
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798.1409));
        assertEquals(Color.web("#9bbcff"), BlackBodyColor.colorForTemperature(39988.149));
        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1001.000149));
        assertThrows(IllegalArgumentException.class, () -> {
            BlackBodyColor.colorForTemperature(40_000.00000000001d);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BlackBodyColor.colorForTemperature(1000d - 0.00000000001d);
        });
    }
}
