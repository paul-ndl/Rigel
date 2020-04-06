package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.awt.*;

import static javafx.application.Application.launch;

public final class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;

    private final Color blue = Color.BLUE;
    private final Color grey = Color.LIGHTGREY;
    private final Color white = Color.WHITE;
    private final Color yellow = Color.YELLOW;
    private final Color opaqueYellow = Color.rgb(255, 255, 0, 0.25);
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
            double radius = 2600*size(sky.stars().get(i).magnitude(), projection)/2; //*2600??????
            ctx.setFill(BlackBodyColor.colorForTemperature(sky.stars().get(i).colorTemperature()));
            ctx.fillOval(transformedCoordinates[2*i]-radius/2, transformedCoordinates[2*i+1]-radius/2, radius, radius);
        }
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        ctx.setFill(Color.GREEN);
        double[] transformedCoordinates = sky.planetPositions();
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, transformedCoordinates.length/2);
        for(int i=0; i<sky.planets().size(); ++i){
            final double radius = 2600*size(sky.planets().get(i).magnitude(), projection)/2;
            ctx.fillOval(transformedCoordinates[2*i], transformedCoordinates[2*i+1]-radius, radius, radius);
        }
    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        double[] transformedCoordinates = {sky.sunPosition().x(), sky.sunPosition().y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        final double radius = 2600*projection.applyToAngle(Angle.ofDeg(0.5))/2;
        ctx.setFill(opaqueYellow);
        ctx.fillOval(transformedCoordinates[0]-radius, transformedCoordinates[0]-radius, 2.2*radius, 2.2*radius);
        ctx.setFill(yellow);
        ctx.fillOval(transformedCoordinates[0]-radius, transformedCoordinates[0]-radius, 2*radius, 2*radius);
        ctx.setFill(white);
        ctx.fillOval(transformedCoordinates[0]-radius, transformedCoordinates[0]-radius, radius, radius);
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas){
        double[] transformedCoordinates = {sky.moonPosition().x(), sky.moonPosition().y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        final double radius = 2600*projection.applyToAngle(Angle.ofDeg(0.5))/2;
        ctx.setFill(red);
        ctx.fillOval(transformedCoordinates[0]-radius, transformedCoordinates[0]-radius, radius, radius);
    }

    public void drawAsterism(ObservedSky sky, Transform planeToCanvas){
        ctx.setStroke(blue);
        double[] previousCoordinates = new double[2];
        for(Asterism a : sky.asterism()){
            int j = 0;
            for(Integer i : sky.asterismIndices(a)){
                double[] transformedCoordinates = {sky.starPositions()[2*i], sky.starPositions()[2*i]};
                planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
                if(j==0) previousCoordinates = transformedCoordinates;
                ctx.moveTo(previousCoordinates[0], previousCoordinates[1]);
                ctx.lineTo(transformedCoordinates[0],transformedCoordinates[1]);
                ctx.stroke();
                previousCoordinates = transformedCoordinates;
            }
        }
    }

    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas, HorizontalCoordinates hor){
        ctx.setStroke(red);
        double[] transformedCoordinates = {projection.circleCenterForParallel(hor).x(), projection.circleCenterForParallel(hor).y()};
        planeToCanvas.transform2DPoints(transformedCoordinates, 0, transformedCoordinates, 0, 1);
        double radius = 2600*projection.circleRadiusForParallel(hor);
        planeToCanvas.deltaTransform(radius, radius);
        ctx.strokeOval(transformedCoordinates[0]-radius, transformedCoordinates[0]-radius, radius, radius);
    }


    private double size(double magnitude, StereographicProjection projection){
        final double clipedMagnitude = interval.clip(magnitude);
        final double function = (99 - 17*clipedMagnitude)/140;
        return function * projection.applyToAngle(Angle.ofDeg(0.5));
    }


}
