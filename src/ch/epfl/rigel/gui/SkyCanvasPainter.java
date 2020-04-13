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
import javafx.scene.transform.Transform;

public final class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    private static final Color BLUE = Color.BLUE;
    private static final Color GREY = Color.LIGHTGREY;
    private static final Color WHITE = Color.WHITE;
    private static final Color YELLOW = Color.YELLOW;
    private static final Color OPAQUE_YELLOW = YELLOW.deriveColor(1, 1, 1, 0.25);
    private static final Color RED = Color.RED;
    private static final Color BLACK = Color.BLACK;

    private static final ClosedInterval interval = ClosedInterval.of(-2, 5);

    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    public void clear() {
        ctx.setFill(BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawAsterism(ObservedSky sky, Transform planeToCanvas) {
        ctx.setStroke(BLUE);
        ctx.setLineWidth(1);
        double[] skyCoordinates = sky.starPositions();
        boolean isInBounds = false;
        Bounds canvasBounds = canvas.getBoundsInLocal();
        for (Asterism a : sky.asterism()) {
            ctx.beginPath();
            int[] indices = sky.asterismIndices(a).stream()
                                                  .mapToInt(Integer :: intValue)
                                                  .toArray();
            for (int i=0; i<indices.length; ++i) {
                int index = indices[i];
                double[] coordinates = {skyCoordinates[2*index], skyCoordinates[2*index + 1]};
                planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, 1);
                ctx.lineTo(coordinates[0], coordinates[1]);
                if (canvasBounds.contains(coordinates[0], coordinates[1]) || isInBounds) {
                    ctx.stroke();
                }
                isInBounds = (canvasBounds.contains(coordinates[0], coordinates[1])) ? true : false;
            }
        }
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] coordinates = sky.starPositions();
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, coordinates.length/2);
        Star[] stars = sky.stars().stream()
                                  .toArray(Star[]::new);
        for (int i=0; i<stars.length; ++i) {
            double diameter = size(stars[i].magnitude(), projection);
            Point2D transform = planeToCanvas.deltaTransform(diameter, 0);
            double trueDiameter = Math.abs(transform.getX());
            ctx.setFill(BlackBodyColor.colorForTemperature(stars[i].colorTemperature()));
            ctx.fillOval(coordinates[2*i]-trueDiameter/2, coordinates[2*i+1]-trueDiameter/2, trueDiameter, trueDiameter);
        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(GREY);
        double[] coordinates = sky.planetPositions();
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, coordinates.length / 2);
        Planet[] planets = sky.planets().stream()
                                        .toArray(Planet[]::new);
        for (int i=0; i<planets.length; ++i) {
            final double diameter = size(planets[i].magnitude(), projection);
            Point2D transform = planeToCanvas.deltaTransform(diameter, 0);
            double trueDiameter = Math.abs(transform.getX());
            ctx.fillOval(coordinates[2*i]-trueDiameter/2, coordinates[2*i+1]-trueDiameter/2, trueDiameter, trueDiameter);
        }
    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double[] coordinates = {sky.sunPosition().x(), sky.sunPosition().y()};
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, 1);
        final double diameter = projection.applyToAngle(sky.sun().angularSize());
        Point2D transform = planeToCanvas.deltaTransform(diameter, 0);
        double trueDiameter = Math.abs(transform.getX());
        ctx.setFill(OPAQUE_YELLOW);
        ctx.fillOval(coordinates[0]-2.2*trueDiameter/2, coordinates[1]-2.2*trueDiameter/2, 2.2*trueDiameter, 2.2*trueDiameter);
        ctx.setFill(YELLOW);
        ctx.fillOval(coordinates[0]-(trueDiameter+2)/2, coordinates[1]-(trueDiameter+2)/2, (trueDiameter+2), (trueDiameter+2));
        ctx.setFill(WHITE);
        ctx.fillOval(coordinates[0]-trueDiameter/2, coordinates[1]-trueDiameter/2, trueDiameter, trueDiameter);
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(WHITE);
        double[] coordinates = {sky.moonPosition().x(), sky.moonPosition().y()};
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, 1);
        final double diameter = projection.applyToAngle(sky.moon().angularSize());
        Point2D transform = planeToCanvas.deltaTransform(diameter, 0);
        double trueDiameter = Math.abs(transform.getX());
        ctx.fillOval(coordinates[0]-trueDiameter/2, coordinates[1]-trueDiameter/2, trueDiameter, trueDiameter);
    }

    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setStroke(RED);
        ctx.setLineWidth(2);
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0, 0));
        double[] coordinates = {center.x(), center.y()};
        planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, 1);
        double diameter = 2 * projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0));
        Point2D point = planeToCanvas.deltaTransform(diameter, 0);
        double trueDiameter = Math.abs(point.getX());
        ctx.strokeOval(coordinates[0]-trueDiameter/2, coordinates[1]-trueDiameter/2, trueDiameter, trueDiameter);
        drawCardinalPoints(projection, planeToCanvas);
    }

    private void drawCardinalPoints(StereographicProjection projection, Transform planeToCanvas) {
        ctx.setFill(Color.RED);
        ctx.setTextBaseline(VPos.TOP);
        for (int i=0; i<8; ++i) {
            HorizontalCoordinates horCoordinates = HorizontalCoordinates.ofDeg(45*i, -0.5);
            CartesianCoordinates projCoordinates = projection.apply(horCoordinates);
            double[] coordinates = {projCoordinates.x(), projCoordinates.y()};
            planeToCanvas.transform2DPoints(coordinates, 0, coordinates, 0, 1);
            String text = horCoordinates.azOctantName("N", "E", "S", "O");
            ctx.fillText(text, coordinates[0], coordinates[1]);
        }
    }

    private double size(double magnitude, StereographicProjection projection) {
        final double clipedMagnitude = interval.clip(magnitude);
        final double function = (99 - 17*clipedMagnitude) / 140;
        return function * projection.applyToAngle(Angle.ofDeg(0.5));
    }


}
