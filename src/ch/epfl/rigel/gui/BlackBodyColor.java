package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BlackBodyColor {

    private final static Charset US_ASCII = StandardCharsets.US_ASCII;
    private final static String BBR_COLOR = ("/bbr_Color.txt");

    private static Map<Integer, Color> loader() throws IOException{
        final Map<Integer, Color> tempColor = new HashMap();
        try(BufferedReader r = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_COLOR), US_ASCII))){
            while(r.ready()) {
                final String line = r.readLine();
                if(line.substring(0,1).equals(" ") &&line.substring(10, 15).equals("10deg")) {
                    Color color = Color.web(line.substring(80,87));
                    tempColor.put((int) Double.parseDouble(line.substring(1, 6)), color);
                }
            }
            return tempColor;
        }
    }

    public static Color colorForTemperature(double temp) throws IOException{
        return loader().get((int) Math.round(temp/100)*100);
    }
}
