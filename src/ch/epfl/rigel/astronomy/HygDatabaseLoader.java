package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE;

    private final static int HIP = 1;
    private final static int PROPER = 6;
    private final static int MAG = 13;
    private final static int CI = 16;
    private final static int RARAD = 23;
    private final static int DECRAD = 24;
    private final static int BAYER = 27;
    private final static int CON = 29;


    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        Charset c = StandardCharsets.US_ASCII;
        String[] columns;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, c))){
            r.readLine();
            while(r.ready()){
                columns = r.readLine().split(",");
                int hip = (!columns[HIP].isEmpty() ? Integer.parseInt(columns[HIP]) : 0);
                String name = ((!columns[PROPER].isEmpty()) ? columns[PROPER] : ((!columns[BAYER].isEmpty() ? columns[BAYER] : "?") + " " + columns[CON]));
                EquatorialCoordinates eq = EquatorialCoordinates.of(Double.parseDouble(columns[RARAD]), Double.parseDouble(columns[DECRAD]));
                Float magnitude = (!columns[MAG].isEmpty() ? Float.parseFloat(columns[MAG]) : 0);
                Float colorIndex = (!columns[CI].isEmpty() ? Float.parseFloat(columns[CI]) : 0);
                builder.addStar(new Star(hip, name, eq, magnitude, colorIndex));
            }
        }
    }
}
