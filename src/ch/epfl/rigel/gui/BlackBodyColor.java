package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BlackBodyColor {

    private BlackBodyColor(){}

    private final static int TEMP_START = 1;
    private final static int TEMP_END = 6;
    private final static int DEG_START = 10;
    private final static int COLOR_START = 80;
    private final static int COLOR_END = 87;

    private final static Interval TEMP_INTERVAL = ClosedInterval.of(1000, 40000);

    private final static Charset US_ASCII = StandardCharsets.US_ASCII;
    private final static String BBR_COLOR = ("/bbr_Color.txt");
    private final static Map<Integer, Color> COLOR_MAP = loader();

    private static Map<Integer, Color> loader(){
        final Map<Integer, Color> tempColor = new HashMap();
        String line;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_COLOR), US_ASCII))){
            while((line = r.readLine()) != null) {
                if(line.startsWith(" ") && line.startsWith("10deg", DEG_START)) {
                    Color color = Color.web(line.substring(COLOR_START, COLOR_END));
                    tempColor.put((int) Double.parseDouble(line.substring(TEMP_START, TEMP_END)), color);
                }
            }
            return tempColor;
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Color colorForTemperature(double temp) {
        Preconditions.checkInInterval(TEMP_INTERVAL, temp);
        return COLOR_MAP.get((int) Math.round(temp/100) * 100);
    }
}
