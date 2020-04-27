package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

public final class SkyCanvasManager {

    private  StarCatalogue catalogue;
    private  DateTimeBean dateTimeBean;
    private  ObserverLocationBean observerLocationBean;
    private ViewingParametersBean viewingParametersBean;
    private Canvas canvas;
    private SkyCanvasPainter painter;
    private  ObservableValue<StereographicProjection> projection;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObservableValue<ObservedSky> observedSky;
    private final ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(null);
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;

    public final ObservableDoubleValue mouseAzDeg;
    public final ObservableDoubleValue mouseAltDeg;
    //public final ObservableValue<CelestialObject> objectUnderMouse;

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
                () -> Transform.affine(dilatation(projection.getValue(), canvas.getWidth(), viewingParametersBean.getFieldOfViewDeg()),
                        0, 0, -dilatation(projection.getValue(), canvas.getWidth(), viewingParametersBean.getFieldOfViewDeg()),
                        canvas.getWidth()/2, canvas.getHeight()/2),
                projection, canvas.widthProperty(), canvas.heightProperty(), viewingParametersBean.fieldOfViewDegProperty());
        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.getValue(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneIdProperty(), observerLocationBean.lonDegProperty(), observerLocationBean.latDegProperty(), projection);
        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> projection.getValue().inverseApply(
                        CartesianCoordinates.of(planeToCanvas.getValue().inverseTransform(getMousePosition().x(), getMousePosition().y()).getX(),
                                                planeToCanvas.getValue().inverseTransform(getMousePosition().x(), getMousePosition().y()).getY())),
                projection, planeToCanvas, mousePositionProperty());
        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.getValue().azDeg(),
                mouseHorizontalPosition);
        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.getValue().altDeg(),
                mouseHorizontalPosition);
    }

    public Canvas canvas(){
        painter.clear();
        System.out.println(canvas.getWidth());
        painter.drawStars(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        return canvas;
    }

    private ReadOnlyObjectProperty<CartesianCoordinates> mousePositionProperty(){
        return mousePosition;
    }

    private CartesianCoordinates getMousePosition(){
        return mousePosition.get();
    }
    private void setMousePosition(CartesianCoordinates mousePosition){
        this.mousePosition.set(mousePosition);
    }

    private double dilatation(StereographicProjection projection, double width, double fieldOfView){
        return width / projection.applyToAngle(Angle.ofDeg(fieldOfView));
    }

}
