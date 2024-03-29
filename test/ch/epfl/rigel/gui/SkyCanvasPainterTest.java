package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;

public final class SkyCanvasPainterTest extends Application {
    public static void main(String[] args) { launch(args); }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    StarCatalogue.Builder builder = new StarCatalogue.Builder();

    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            builder = builder.loadFrom(hs, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = builder
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();


            long time0 = 0;
            long timeAvg = 0;

            ZonedDateTime when =
                    ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
            GeographicCoordinates where =
                    GeographicCoordinates.ofDeg(6.57, 46.52);
            HorizontalCoordinates projCenter =
                    HorizontalCoordinates.ofDeg(277, -23);
            StereographicProjection projection =
                    new StereographicProjection(projCenter);
            ObservedSky sky =
                    new ObservedSky(when, where, projection, catalogue);

            Canvas canvas =
                    new Canvas(800, 600);
            Transform planeToCanvas =
                    Transform.affine(1300, 0, 0, -1300, 400, 300);
            SkyCanvasPainter painter =
                    new SkyCanvasPainter(canvas);

            time0 = System.nanoTime();

            painter.clear();
            painter.drawStars(sky, projection, planeToCanvas);
            painter.drawPlanets(sky, projection, planeToCanvas);
            painter.drawSun(sky, projection, planeToCanvas);
            painter.drawMoon(sky, projection, planeToCanvas);
            painter.drawHorizon(projection, planeToCanvas);

            timeAvg = System.nanoTime() - time0;

            System.out.println((timeAvg / 1000000d)+" in milliseconds"); //PERFORMANCE BENCH

            WritableImage fxImage =
                    canvas.snapshot(null, null);
            BufferedImage swingImage =
                    SwingFXUtils.fromFXImage(fxImage, null);
            ImageIO.write(swingImage, "png", new File("sky.png"));
        }
        Platform.exit();
    }
}
