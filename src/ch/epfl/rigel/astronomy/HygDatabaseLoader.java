package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Un chargeur de catalogue HYG
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    private final static int HIP = 1;
    private final static int PROPER = 6;
    private final static int MAG = 13;
    private final static int CI = 16;
    private final static int RARAD = 23;
    private final static int DECRAD = 24;
    private final static int BAYER = 27;
    private final static int CON = 29;
    private final static Charset US_ASCII = StandardCharsets.US_ASCII;

    /**
     * Charge les étoiles du flot d'entrée et les ajoute au bâtisseur
     *
     * @param inputStream le flot d'entrée
     * @param builder     le bâtisseur
     * @throws IOException en cas d'erreur entrée/sortie
     * @see ch.epfl.rigel.astronomy.StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] columns = line.split(",");
                final int hip = (!columns[HIP].isEmpty() ? Integer.parseInt(columns[HIP]) : 0);
                final String name = ((!columns[PROPER].isEmpty()) ? columns[PROPER] : ((!columns[BAYER].isEmpty() ? columns[BAYER] : "?") + " " + columns[CON]));
                final EquatorialCoordinates eq = EquatorialCoordinates.of(Double.parseDouble(columns[RARAD]), Double.parseDouble(columns[DECRAD]));
                final float magnitude = (!columns[MAG].isEmpty() ? Float.parseFloat(columns[MAG]) : 0);
                final float colorIndex = (!columns[CI].isEmpty() ? Float.parseFloat(columns[CI]) : 0);
                builder.addStar(new Star(hip, name, eq, magnitude, colorIndex));
            }
        }
    }
}
