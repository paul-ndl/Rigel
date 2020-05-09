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
import java.util.List;

public final class CityLoader {

        private final static int NAME = 1;
        private final static int LON = 2;
        private final static int LAT = 3;
        private final static int ZONE = 4;
        private final static String CITY = "/city.csv";
        private final static Charset US_ASCII = StandardCharsets.US_ASCII;
        private final static List<City> CITIES_LIST = loader();

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
                    String zone = columns[ZONE];
                    list.add(new City(name, lonDeg, latDeg, zone));
                }
                return list;
            } catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public static List<Point3D> geoCoordTo3dCoord(){
            List<Point3D> citiesCoord = new ArrayList<>();
            for(City c : CITIES_LIST){
                double lon = c.getLonDeg()+2.8;
                double lat = c.getLatDeg()-0.2;
                double x = -Math.sin(Angle.ofDeg(lon)) * Math.cos(Angle.ofDeg(lat));
                double y = -Math.sin(Angle.ofDeg(lat));
                double z = Math.cos(Angle.ofDeg(lon)) * Math.cos(Angle.ofDeg(lat));
                citiesCoord.add(new Point3D(x, y, z));
            }
            return citiesCoord;
        }
}
