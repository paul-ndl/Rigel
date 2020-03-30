package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.MoonModel;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.PlanetModel;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.astronomy.SunModel;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public class ObservedSkyTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String AST_CATALOGUE_NAME =
            "/asterisms.txt";
    private static StarCatalogue catalogue;
    private static ObservedSky sky;
    private static StereographicProjection stereo;
    private static GeographicCoordinates geoCoords;
    private static ZonedDateTime observationTime;
    private static EquatorialToHorizontalConversion convEquToHor;
    private static EclipticToEquatorialConversion convEcltoEqu;

    @Test
    void init() throws IOException {

        if (catalogue == null) {
            StarCatalogue.Builder builder;
            try (InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)) {
                builder = new StarCatalogue.Builder()
                        .loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
            }
            try (InputStream astStream = getClass()
                    .getResourceAsStream(AST_CATALOGUE_NAME)) {
                catalogue = builder
                        .loadFrom(astStream, AsterismLoader.INSTANCE)
                        .build();
            }

            observationTime = ZonedDateTime.of(
                    LocalDate.of(2020, Month.APRIL, 4),
                    LocalTime.of(0, 0), ZoneOffset.UTC
            );

            geoCoords = GeographicCoordinates.ofDeg(30, 45);

            stereo = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));

            convEquToHor = new EquatorialToHorizontalConversion(observationTime, geoCoords);

            convEcltoEqu = new EclipticToEquatorialConversion(observationTime);

            sky = new ObservedSky(observationTime, geoCoords, stereo, catalogue);
        }
    }

    @Test
    void objectClosestToWorks() throws IOException {
        init();
        long time0 = 0;
        long timeAvg = 0;
        long total = 0;
        for (Asterism asterism : catalogue.asterisms()) {
            total += asterism.stars().size();
            for (Star star : asterism.stars()) {
                time0 = System.nanoTime();
                assertEquals(star.name(), sky.objectClosestTo(stereo.apply(convEquToHor.apply(star.equatorialPos())),
                        Double.MAX_VALUE).get().name());

                timeAvg += System.nanoTime() - time0;

                //Rater le test ci-dessous = il faut mettre un <= distanceMax au lieu de < distanceMax
                assertEquals(star.name(), sky.objectClosestTo(stereo.apply(convEquToHor.apply(star.equatorialPos())),
                        0).get().name());

                assertEquals(Optional.empty(), sky.objectClosestTo(stereo.apply(convEquToHor.apply(star.equatorialPos())),
                        -1));
            }
        }
        //System.out.println((timeAvg / (total * 1000000d))+" in milliseconds"); //PERFORMANCE BENCH
    }

    @Test
    void stars() throws IOException {
        init();
        int i = 0;
        for (Star star : sky.stars()) {
            assertEquals(stereo.apply(convEquToHor.apply(star.equatorialPos())).x(),
                    sky.starPositions()[i]);
            i += 2;
        }
        assertEquals(catalogue.stars().size(),sky.stars().size());

        //Si fail: Cloner le tableau
        double memory = sky.starPositions()[0];
        sky.starPositions()[0] = Double.MAX_VALUE;
        assertEquals(memory, sky.starPositions()[0]);
    }

    @Test
    void planets() throws IOException {
        init();
        assertEquals(14, sky.planetPositions().length);
        int i = 0;
        for (Planet planet : sky.planets()) {
                assertEquals(stereo.apply(convEquToHor.apply(planet.equatorialPos())).x(),
                        sky.planetPositions()[i++]);
                assertEquals(stereo.apply(convEquToHor.apply(planet.equatorialPos())).y(),
                        sky.planetPositions()[i++]);
        }

        //Si fail: Cloner le tableau
        double memory = sky.planetPositions()[0];
        sky.planetPositions()[0] = Double.MAX_VALUE;
        assertEquals(memory, sky.planetPositions()[0]);
    }

    @Test
    void moonAndSun() throws IOException {
        init();
        assertEquals(SunModel.SUN.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).eclipticPos().lon(),
                sky.sun().eclipticPos().lon());
        //Sun possède le getter equatorialPos mais autant tester la précision avec 2 conversions successives...
        assertEquals(stereo.apply(convEquToHor.apply(convEcltoEqu.apply(SunModel.SUN.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).eclipticPos()))).x(),
                sky.sunPosition().x());
        assertEquals(stereo.apply(convEquToHor.apply(convEcltoEqu.apply(SunModel.SUN.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).eclipticPos()))).y(),
                sky.sunPosition().y());

        assertEquals(MoonModel.MOON.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).equatorialPos().dec(),
                sky.moon().equatorialPos().dec());
        assertEquals(stereo.apply(convEquToHor.apply(MoonModel.MOON.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).equatorialPos())).x(),
                sky.moonPosition().x());
        assertEquals(stereo.apply(convEquToHor.apply(MoonModel.MOON.at(Epoch.J2010.daysUntil(observationTime),convEcltoEqu).equatorialPos())).y(),
                sky.moonPosition().y());
    }
}


