package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Transform;

public final class SkyCanvasManager {

    private final StarCatalogue catalogue;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    private Canvas canvas;
    private SkyCanvasPainter painter;
    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObservableValue<ObservedSky> observedSky;

    private final ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(null);
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;

    public final ObservableDoubleValue mouseAzDeg;
    public final ObservableDoubleValue mouseAltDeg;
    public final ObservableValue<CelestialObject> objectUnderMouse;

    private static final ClosedInterval FOV = ClosedInterval.of(30,150);
    private static final RightOpenInterval AZIMUT_INTERVAL = RightOpenInterval.of(0, 360);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(-90, 90);

    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean){
        this.catalogue = catalogue;
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        canvas = new Canvas();
        painter = new SkyCanvasPainter(canvas);
        setMousePosition(CartesianCoordinates.of(400,300));

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(
                () -> Transform.affine(dilatation(getProjection(), canvas.getWidth(), viewingParametersBean.getFieldOfViewDeg()),
                        0, 0, -dilatation(getProjection(), canvas.getWidth(), viewingParametersBean.getFieldOfViewDeg()),
                        canvas.getWidth()/2, canvas.getHeight()/2),
                projectionProperty(), viewingParametersBean.fieldOfViewDegProperty());

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), getProjection(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneIdProperty(), observerLocationBean.lonDegProperty(), observerLocationBean.latDegProperty(), projectionProperty());


        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> getProjection().inverseApply(
                        CartesianCoordinates.of(getPlaneToCanvas().inverseTransform(getMousePosition().x(), getMousePosition().y()).getX(),
                                                getPlaneToCanvas().inverseTransform(getMousePosition().x(), getMousePosition().y()).getY())),
                projectionProperty(), planeToCanvasProperty(), mousePositionProperty());


        mouseAzDeg = Bindings.createDoubleBinding(
                () -> getMouseHorizontalPosition().azDeg(),
                mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> getMouseHorizontalPosition().altDeg(),
                mouseHorizontalPosition);

        objectUnderMouse = Bindings.createObjectBinding(
                () -> getObservedSky().objectClosestTo(
                        CartesianCoordinates.of(getPlaneToCanvas().inverseTransform(getMousePosition().x(), getMousePosition().y()).getX(),
                                                getPlaneToCanvas().inverseTransform(getMousePosition().x(), getMousePosition().y()).getY()), 10).get(),
                planeToCanvas, observedSky, mousePosition);

        canvas.setOnKeyPressed(e -> {
            if(canvas.isFocused()){
                if(e.getCode() == KeyCode.UP){
                    if(ALT_INTERVAL.contains(viewingParametersBean.getCenter().altDeg()+5))
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(), viewingParametersBean.getCenter().altDeg()+5));
                }
                if(e.getCode() == KeyCode.DOWN){
                    if(ALT_INTERVAL.contains(viewingParametersBean.getCenter().altDeg()-5))
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(), viewingParametersBean.getCenter().altDeg()-5));
                }
                if(e.getCode() == KeyCode.RIGHT){
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZIMUT_INTERVAL.reduce(viewingParametersBean.getCenter().azDeg()+10), viewingParametersBean.getCenter().altDeg()));
                }
                if(e.getCode() == KeyCode.LEFT){
                    viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(AZIMUT_INTERVAL.reduce(viewingParametersBean.getCenter().azDeg()-10), viewingParametersBean.getCenter().altDeg()));
                }
            }
            paint();
        });

        canvas.setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown()){
                canvas.requestFocus();
            }
        });

        canvas.setOnMouseMoved(e -> setMousePosition(CartesianCoordinates.of(e.getX(), e.getY())));

        canvas.setOnScroll(e -> {
            if(Math.abs(e.getDeltaX())>Math.abs(e.getDeltaY())){
                double zoom = viewingParametersBean.getFieldOfViewDeg()+e.getDeltaX();
                if(FOV.contains(zoom))
                viewingParametersBean.setFieldOfViewDeg(zoom);
            } else {
                double zoom = viewingParametersBean.getFieldOfViewDeg()+e.getDeltaY();
                if(FOV.contains(zoom))
                    viewingParametersBean.setFieldOfViewDeg(zoom);
            }
            paint();
        });
    }

    public Canvas canvas(){
        return canvas;
    }

    private void paint(){
        painter.clear();
        painter.drawStars(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawPlanets(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawSun(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawMoon(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawHorizon(getProjection(), getPlaneToCanvas());
    }

    private ObservableValue<StereographicProjection> projectionProperty(){
        return projection;
    }

    private StereographicProjection getProjection(){
        return projection.getValue();
    }

    private ObservableValue<Transform> planeToCanvasProperty(){
        return planeToCanvas;
    }

    private Transform getPlaneToCanvas(){
        return planeToCanvas.getValue();
    }

    private ObservableValue<ObservedSky> observedSkyProperty(){
        return observedSky;
    }

    private ObservedSky getObservedSky(){
        return observedSky.getValue();
    }

    private ObjectProperty<CartesianCoordinates> mousePositionProperty(){
        return mousePosition;
    }

    private CartesianCoordinates getMousePosition(){
        return mousePosition.get();
    }

    private void setMousePosition(CartesianCoordinates mousePosition){
        this.mousePosition.set(mousePosition);
    }

    private ObservableValue<HorizontalCoordinates> mouseHorizontalPositionProperty(){
        return mouseHorizontalPosition;
    }

    private HorizontalCoordinates getMouseHorizontalPosition(){
        return mouseHorizontalPosition.getValue();
    }

    private ObservableDoubleValue mouseAzDegProperty(){
        return mouseAzDeg;
    }

    private double getMouseAzDeg(){
        return mouseAzDeg.get();
    }

    private ObservableDoubleValue mouseAltProperty(){
        return mouseAltDeg;
    }

    private double getMouseAltDeg(){
        return mouseAltDeg.get();
    }

    public ObservableValue<CelestialObject> objectUnderMouseProperty(){
        return objectUnderMouse;
    }

    public CelestialObject getObjectUnderMouse(){
        return objectUnderMouse.getValue();
    }

    private double dilatation(StereographicProjection projection, double width, double fieldOfView){
        return width / projection.applyToAngle(Angle.ofDeg(fieldOfView));
    }

}
