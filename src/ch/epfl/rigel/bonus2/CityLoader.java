package ch.epfl.rigel.bonus2;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CityLoader {

        private final static int NAME = 2;
        private final static int LON = 5;
        private final static int LAT = 4;
        private final static int ZONE = 6;
        private final static String CITY = "/city.csv";
        private final static Charset US_ASCII = StandardCharsets.US_ASCII;
        private final static List<City> CITIES_LIST = loader();
        public final static Map<GeographicCoordinates, String> CITIES_MAP = getMap();

        private static List<City> loader(){
            List<City> list = new ArrayList<>();
            String line;
            try(BufferedReader r = new BufferedReader(new InputStreamReader(CityLoader.class.getResourceAsStream(CITY), US_ASCII))){
                r.readLine();
                while((line = r.readLine()) != null) {
                    String[] columns = line.split(";");
                    String name = columns[NAME];
                    double lonDeg = Double.parseDouble(columns[LON]);
                    double latDeg = Double.parseDouble(columns[LAT]);
                    double zone = Double.parseDouble(columns[ZONE]);
                    list.add(new City(name, lonDeg, latDeg, zone));
                }
                return list;
            } catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private static Map<GeographicCoordinates, String> getMap(){
            Map<GeographicCoordinates, String> citiesCoord = new HashMap<>();
            for(City c : CITIES_LIST){
                citiesCoord.put(GeographicCoordinates.ofDeg(c.getLonDeg(), c.getLatDeg()), c.getName());
            }
            return citiesCoord;
        }
}
