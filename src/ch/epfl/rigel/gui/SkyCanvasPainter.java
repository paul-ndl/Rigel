package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public final class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    private final Color blue = Color.BLUE;
    private final Color grey = Color.LIGHTGREY;
    private final Color white = Color.WHITE;
    private final Color yellow = Color.YELLOW;
    private final Color opaqueYellow = yellow.deriveColor(1,1,1,0.25);
    private final Color red = Color.RED;
    private final Color black = Color.BLACK;

    private final ClosedInterval interval = ClosedInterval.of(-2, 5);

    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    public void clear(){
        ctx.setFill(black);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        double[] transformedCoordinates = sky.starPositions();
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, transformedCoordinates.length/2);
        for(int i=0; i<sky.stars().size(); ++i){
            double radius = size(sky.stars().get(i).magnitude(), projection)/2;
            Point2D point = planeToCanvas.deltaTransform(radius, radius);
            double trueRadius = Math.abs(point.getX()) + Math.abs(point.getY());
            ctx.setFill(BlackBodyColor.colorForTemperature(sky.stars().get(i).colorTemperature()));
            ctx.fillOval(transformedCoordinates[2*i]-trueRadius/2, transformedCoordinates[2*i+1]-trueRadius/2, trueRadius, trueRadius);
        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        ctx.setFill(grey);
        double[] transformedCoordinates = sky.planetPositions();
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, transformedCoordinates.length/2);
        for(int i=0; i<sky.planets().size(); ++i){
            final double radius = size(sky.planets().get(i).magnitude(), projection)/2;
            Point2D point = planeToCanvas.deltaTransform(radius, radius);
            double trueRadius = Math.abs(point.getX()) + Math.abs(point.getY());
            ctx.fillOval(transformedCoordinates[2*i]-trueRadius/2, transformedCoordinates[2*i+1]-trueRadius/2, trueRadius, trueRadius);
        }
    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        double[] transformedCoordinates = {sky.sunPosition().x(), sky.sunPosition().y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        final double radius = projection.applyToAngle(Angle.ofDeg(0.5))/2;
        Point2D point = planeToCanvas.deltaTransform(radius, radius);
        double trueRadius = Math.abs(point.getX()) + Math.abs(point.getY());
        ctx.setFill(opaqueYellow);
        ctx.fillOval(transformedCoordinates[0]-2.2*trueRadius/2, transformedCoordinates[1]-2.2*trueRadius/2, 2.2*trueRadius, 2.2*trueRadius);
        ctx.setFill(yellow);
        ctx.fillOval(transformedCoordinates[0]-(trueRadius+2)/2, transformedCoordinates[1]-(trueRadius+2)/2, (trueRadius+2), (trueRadius+2));
        ctx.setFill(white);
        ctx.fillOval(transformedCoordinates[0]-trueRadius/2, transformedCoordinates[1]-trueRadius/2, trueRadius, trueRadius);
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        double[] transformedCoordinates = {sky.moonPosition().x(), sky.moonPosition().y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        final double radius = projection.applyToAngle(Angle.ofDeg(0.5))/2;
        Point2D point = planeToCanvas.deltaTransform(radius, radius);
        double trueRadius = Math.abs(point.getX()) + Math.abs(point.getY());
        ctx.setFill(white);
        ctx.fillOval(transformedCoordinates[0]-trueRadius/2, transformedCoordinates[1]-trueRadius/2, radius, radius);
    }

    public void drawAsterism(ObservedSky sky, Transform planeToCanvas){
        ctx.setStroke(blue);
        ctx.setLineWidth(1);
        double[] previousCoordinates = new double[2];
        double[] actualCoordinates = new double[2];
        for(Asterism a : sky.asterism()){
            for(int i=0; i<a.stars().size(); ++i){
                final int index = sky.asterismIndices(a).get(i);
                actualCoordinates[0] = sky.starPositions()[2*index];
                actualCoordinates[1] = sky.starPositions()[2*index + 1];
                planeToCanvas.transform2DPoints(actualCoordinates, 0, actualCoordinates, 0, 1);
                if(i==0){
                    ctx.moveTo(actualCoordinates[0], actualCoordinates[1]);
                    previousCoordinates = actualCoordinates;
                }
                ctx.lineTo(actualCoordinates[0], actualCoordinates[1]);
                if(canvas.getBoundsInLocal().contains(actualCoordinates[0], actualCoordinates[1]) || canvas.getBoundsInLocal().contains(previousCoordinates[0], previousCoordinates[1])){
                    ctx.stroke();
                }
                previousCoordinates = actualCoordinates;
            }
        }
    }

    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas){
        ctx.setStroke(red);
        ctx.setLineWidth(2);
        CartesianCoordinates coordinates = projection.circleCenterForParallel(HorizontalCoordinates.of(0,0));
        double[] transformedCoordinates = {coordinates.x(), coordinates.y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.of(0,0));
        Point2D point = planeToCanvas.deltaTransform(radius, radius);
        double trueRadius = Math.abs(point.getX()) + Math.abs(point.getY());
        ctx.strokeOval(transformedCoordinates[0]-trueRadius/2, transformedCoordinates[1]-trueRadius/2, trueRadius, trueRadius);
    }


    private double size(double magnitude, StereographicProjection projection){
        final double clipedMagnitude = interval.clip(magnitude);
        final double function = (99 - 17*clipedMagnitude) / 140;
        return function * projection.applyToAngle(Angle.ofDeg(0.5));
    }


}
