package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
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

    private final static Charset US_ASCII = StandardCharsets.US_ASCII;
    private final static String BBR_COLOR = ("/bbr_Color.txt");
    private final static Map<Integer, Color> COLOR_MAP = loader();

    private static Map<Integer, Color> loader(){
        final Map<Integer, Color> tempColor = new HashMap();
        String line;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_COLOR), US_ASCII))){
            while((line = r.readLine())!=null) {
                if(line.substring(0,1).equals(" ") && line.substring(10, 15).equals("10deg")) {
                    Color color = Color.web(line.substring(80,87));
                    tempColor.put((int) Double.parseDouble(line.substring(1, 6)), color);
                }
            }
            return tempColor;
        }
        catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }

    public static Color colorForTemperature(double temp) {
        Preconditions.checkInInterval(ClosedInterval.of(1000, 40000), temp);
        return COLOR_MAP.get((int) Math.round(temp/100)*100);
    }
}
