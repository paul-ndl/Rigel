package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AsterismLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String AST_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void loadWorks() throws IOException {
        StarCatalogue.Builder builder;
        Queue<Asterism> a = new ArrayDeque<>();
        Star beltegeuse = null;
        StarCatalogue catalogue;
        long time0 = 0;
        long timeAvg = 0;
        long total = 0;
        Asterism falseAst = new Asterism(Arrays.asList(new Star(0, "Rigel", EquatorialCoordinates.of(0,0), 0f, 0f)));
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            builder = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream astStream = getClass()
                .getResourceAsStream(AST_CATALOGUE_NAME)) {
            time0 = System.nanoTime();
             catalogue = builder
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();
            timeAvg += System.nanoTime() - time0;
            for (Asterism ast : catalogue.asterisms()) {
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Rigel")) {
                        a.add(ast);
                    }
                }
            }
            for (Asterism ast : a) {
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Betelgeuse")) {
                        beltegeuse = s;
                    }
                }
            }
            assertThrows(UnsupportedOperationException.class, () ->{
                catalogue.stars().clear();
            });
            assertThrows(UnsupportedOperationException.class, () ->{
                catalogue.asterisms().clear();
            });
            assertNotNull(beltegeuse);
            assertThrows(IllegalArgumentException.class, () -> {
                catalogue.asterismIndices(falseAst);
            });
        }
        System.out.println((timeAvg / (1000000d))+" in milliseconds"); //PERFORMANCE BENCH
    }


}
