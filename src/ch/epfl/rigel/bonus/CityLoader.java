package ch.epfl.rigel.bonus;

import ch.epfl.rigel.math.Angle;
import javafx.geometry.Point3D;

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
        public final static Map<Point3D, City> CITIES_MAP = geoCoordTo3dCoord();

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

        private static Map<Point3D, City> geoCoordTo3dCoord(){
            Map<Point3D, City> citiesCoord = new HashMap<>();
            for(City c : CITIES_LIST){
                double lon = (c.getLonDeg()+2.8);
                double lat = (c.getLatDeg()-0.2);
                double x = -Math.sin(Angle.ofDeg(lon)) * Math.cos(Angle.ofDeg(lat));
                double y = -Math.sin(Angle.ofDeg(lat));
                double z = Math.cos(Angle.ofDeg(lon)) * Math.cos(Angle.ofDeg(lat));
                citiesCoord.put(new Point3D(x, y, z), c);
            }
            return citiesCoord;
        }
}
