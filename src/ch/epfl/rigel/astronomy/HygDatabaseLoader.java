package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

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
                String[] columns = line.split(",");
                int hip = valueOrDefaultInt(columns, HIP);
                String name = valueOrDefaultString(columns, PROPER, BAYER, CON);
                EquatorialCoordinates eq = EquatorialCoordinates.of(Double.parseDouble(columns[RARAD]), Double.parseDouble(columns[DECRAD]));
                float magnitude = valueOrDefaultFloat(columns, MAG);
                float colorIndex = valueOrDefaultFloat(columns, CI);
                builder.addStar(new Star(hip, name, eq, magnitude, colorIndex));
            }
        }
    }

    private int valueOrDefaultInt(String[] columns, int index){
        return !columns[index].isEmpty() ? Integer.parseInt(columns[index]) : 0;
    }

    private String valueOrDefaultString(String[] columns, int proper, int bayer, int con){
        return (!columns[proper].isEmpty()) ? columns[proper] : ((!columns[bayer].isEmpty() ? columns[bayer] : "?") + " " + columns[con]);
    }

    private float valueOrDefaultFloat(String[] columns, int index){
        return !columns[index].isEmpty() ? Float.parseFloat(columns[index]) : 0;
    }
}
