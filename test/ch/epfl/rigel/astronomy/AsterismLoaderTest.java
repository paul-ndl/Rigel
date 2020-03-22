package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AsterismLoaderTest {

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String AST_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void loadWorks() throws IOException {
        StarCatalogue.Builder builder;
        Asterism a = null;
        Star betelgeuse = null;
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            builder = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
            }
        try (InputStream astStream = getClass()
                .getResourceAsStream(AST_CATALOGUE_NAME)) {
            StarCatalogue catalogue = builder
                    .loadFrom(astStream, AsterismLoader.INSTANCE)
                    .build();
            for(Asterism ast : catalogue.asterisms()){
                for(Star s : ast.stars()){
                    if(s.name().equalsIgnoreCase("rigel")){
                        a = ast;
                    }
                }
            }
            for(Star s : a.stars()){
                if(s.name().equalsIgnoreCase("Betelgeuse")){
                    betelgeuse = s;
                }
            }
            assertNotNull(betelgeuse);
            }
        }


}
