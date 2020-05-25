package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Un chargeur de catalogue d'astérismes
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;

    /**
     * Charge les astérismes du flot d'entrée et les ajoute au bâtisseur
     *
     * @param inputStream le flot d'entrée
     * @param builder     le bâtisseur
     * @throws IOException en cas d'erreur entrée/sortie
     * @see ch.epfl.rigel.astronomy.StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        Map<Integer, Star> map = new HashMap<>();
        for (Star s : builder.stars()) {
            map.put(s.hipparcosId(), s);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Star> stars = new ArrayList<>();
                String[] hip = line.split(",");
                for (String h : hip) {
                    stars.add(map.get(Integer.parseInt(h)));
                }
                builder.addAsterism(new Asterism(stars));
            }
        }
    }
}
