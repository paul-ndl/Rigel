package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Asterism falseAst = new Asterism(Arrays.asList(new Star(0, "Rigel", EquatorialCoordinates.of(0,0), 0f, 0f)));
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
            for (Asterism ast : catalogue.asterisms()) {
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Rigel")) {
                        a.add(ast);
                    }
                }
            }
            for (Asterism ast : a) {
                System.out.println(catalogue.asterismIndices(ast));
                for (Star s : ast.stars()) {
                    if (s.name().equalsIgnoreCase("Betelgeuse")) {
                        beltegeuse = s;
                    }
                }
            }
            assertNotNull(beltegeuse);
            assertThrows(IllegalArgumentException.class, () -> {
                catalogue.asterismIndices(falseAst);
            });
        }
    }


}
