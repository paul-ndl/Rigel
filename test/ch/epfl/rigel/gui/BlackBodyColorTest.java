package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static ch.epfl.rigel.gui.BlackBodyColor.colorForTemperature;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlackBodyColorTest {

    @Test
    void colorForTemperatureWorks() {
        assertEquals(Color.rgb(0xc7,0xd8, 0xff), colorForTemperature(10600));
    }
}
