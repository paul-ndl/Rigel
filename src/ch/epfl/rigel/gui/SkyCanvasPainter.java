package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Set;

/**
 * Un peintre de ciel
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext ctx;

    private static final Color BLUE = Color.BLUE;
    private static final Color GREY = Color.LIGHTGREY;
    private static final Color WHITE = Color.WHITE;
    private static final Color YELLOW = Color.YELLOW;
    private static final Color OPAQUE_YELLOW = YELLOW.deriveColor(1, 1, 1, 0.25);
    private static final Color RED = Color.RED;
    private static final Color BLACK = Color.BLACK;

    private static final double SUN_ANGULAR_SIZE = Angle.ofDeg(0.5);
    private static final double SUN_BIG_DIAMETER = 2.2;
    private static final double SUN_MEDIUM_DIAMETER = 2;

    private static final ClosedInterval CLIP_INTERVAL = ClosedInterval.of(-2, 5);

    /**
     * Construit un peintre de ciel sur le canvas donné
     *
     * @param canvas le canvas
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Nettoie le canvas avec un ciel noir
     */
    public void clear() {
        ctx.setFill(BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Dessine les étoiles et astérismes du ciel observé donné par rapport
     * à la projection et la transformation à appliquer
     *
     * @param sky           le ciel observé
     * @param projection    la projection stéréographique
     * @param planeToCanvas la transformation
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        List<Star> stars = sky.stars();
        double[] coordinates = sky.starPositions();
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, coordinates.length / 2);
        //Construction des astérisms
        ctx.setStroke(BLUE);
        ctx.setLineWidth(1);
        Set<Asterism> asterisms = sky.asterism();
        Bounds canvasBounds = canvas.getBoundsInLocal();
        boolean previousInBounds = false;
        for (Asterism a : asterisms) {
            ctx.beginPath();
            List<Integer> indices = sky.asterismIndices(a);
            for (Integer i : indices) {
                double x = coordinates[2 * i];
                double y = coordinates[2 * i + 1];
                if (canvasBounds.contains(x, y) || previousInBounds) {
                    ctx.lineTo(x, y);
                } else {
                    ctx.moveTo(x, y);
                }
                previousInBounds = (canvasBounds.contains(x, y));
            }
            ctx.stroke();
        }
        //Construction des étoiles
        for (int i = 0; i < stars.size(); ++i) {
            double diameter = size(stars.get(i).magnitude(), projection);
            double trueDiameter = planeToCanvas.deltaTransform(diameter, 0).magnitude();
            ctx.setFill(BlackBodyColor.colorForTemperature(stars.get(i).colorTemperature()));
            ctx.fillOval(coordinates[2 * i] - trueDiameter / 2, coordinates[2 * i + 1] - trueDiameter / 2, trueDiameter, trueDiameter);
        }
    }

    /**
     * Dessine les planètes du ciel observé donné par rapport
     * à la projection et la transformation à appliquer
     *
     * @param sky           le ciel observé
     * @param projection    la projection stéréographique
     * @param planeToCanvas la transformation
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        List<Planet> planets = sky.planets();
        double[] coordinates = sky.planetPositions();
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, coordinates.length / 2);
        ctx.setFill(GREY);
        for (int i = 0; i < planets.size(); ++i) {
            double diameter = size(planets.get(i).magnitude(), projection);
            double trueDiameter = planeToCanvas.deltaTransform(diameter, 0).magnitude();
            ctx.fillOval(coordinates[2 * i] - trueDiameter / 2, coordinates[2 * i + 1] - trueDiameter / 2, trueDiameter, trueDiameter);
        }
    }

    /**
     * Dessine le Soleil du ciel observé donné par rapport
     * à la projection et la transformation à appliquer
     *
     * @param sky           le ciel observé
     * @param projection    la projection stéréographique
     * @param planeToCanvas la transformation
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        CartesianCoordinates position = sky.sunPosition();
        Point2D coordinates = planeToCanvas.transform(position.x(), position.y());
        double x = coordinates.getX();
        double y = coordinates.getY();
        double diameter = projection.applyToAngle(sky.sun().angularSize());
        double trueDiameter = planeToCanvas.deltaTransform(diameter, 0).magnitude();
        double bigDiameter = SUN_BIG_DIAMETER * trueDiameter;
        double mediumDiameter = SUN_MEDIUM_DIAMETER + trueDiameter;
        ctx.setFill(OPAQUE_YELLOW);
        ctx.fillOval(x - bigDiameter / 2, y - bigDiameter / 2, bigDiameter, bigDiameter);
        ctx.setFill(YELLOW);
        ctx.fillOval(x - mediumDiameter / 2, y - mediumDiameter / 2, mediumDiameter, mediumDiameter);
        ctx.setFill(WHITE);
        ctx.fillOval(x - trueDiameter / 2, y - trueDiameter / 2, trueDiameter, trueDiameter);
    }

    /**
     * Dessine la Lune du ciel observé donné par rapport
     * à la projection et la transformation à appliquer
     *
     * @param sky           le ciel observé
     * @param projection    la projection stéréographique
     * @param planeToCanvas la transformation
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(WHITE);
        CartesianCoordinates position = sky.moonPosition();
        Point2D coordinates = planeToCanvas.transform(position.x(), position.y());
        double diameter = projection.applyToAngle(sky.moon().angularSize());
        double trueDiameter = planeToCanvas.deltaTransform(diameter, 0).magnitude();
        ctx.fillOval(coordinates.getX() - trueDiameter / 2, coordinates.getY() - trueDiameter / 2, trueDiameter, trueDiameter);
    }

    /**
     * Dessine l'horizon et les points (inter)cardinaux par rapport
     * à la projection et la transformation à appliquer
     *
     * @param projection    la projection stéréographique
     * @param planeToCanvas la transformation
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(RED);
        ctx.setLineWidth(2);
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        Point2D coordinates = planeToCanvas.transform(center.x(), center.y());
        double diameter = 2 * projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));
        double trueDiameter = planeToCanvas.deltaTransform(diameter, 0).magnitude();
        ctx.strokeOval(coordinates.getX() - trueDiameter / 2, coordinates.getY() - trueDiameter / 2, trueDiameter, trueDiameter);
        drawCardinalPoints(projection, planeToCanvas);
    }

    private void drawCardinalPoints(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(Color.RED);
        ctx.setTextBaseline(VPos.TOP);
        ctx.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < 8; ++i) {
            HorizontalCoordinates horCoordinates = HorizontalCoordinates.ofDeg(45 * i, -0.5);
            CartesianCoordinates projCoordinates = projection.apply(horCoordinates);
            Point2D coordinates = planeToCanvas.transform(projCoordinates.x(), projCoordinates.y());
            String text = horCoordinates.azOctantName("N", "E", "S", "O");
            ctx.fillText(text, coordinates.getX(), coordinates.getY());
        }
    }

    private double size(double magnitude, StereographicProjection projection) {
        final double clipedMagnitude = CLIP_INTERVAL.clip(magnitude);
        final double function = (99 - 17 * clipedMagnitude) / 140;
        return function * projection.applyToAngle(SUN_ANGULAR_SIZE);
    }

}
