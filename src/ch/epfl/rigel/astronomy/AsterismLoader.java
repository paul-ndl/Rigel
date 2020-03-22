package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        Charset c = StandardCharsets.US_ASCII;
        String[] hip;
        List<Star> catalogue = builder.stars();
        List<Star> stars = new ArrayList();
        Map<Integer, Star> map = new HashMap<>();
        for (Star s : catalogue){
            map.put(s.hipparcosId(), s);
        }
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, c))){
            while(r.ready()){
                hip = r.readLine().split(",");
                for (String h : hip) {
                    stars.add(map.get(Integer.parseInt(h)));
                }
                builder.addAsterism(new Asterism(List.copyOf(stars)));
                stars.clear();
            }
        }
    }
}
