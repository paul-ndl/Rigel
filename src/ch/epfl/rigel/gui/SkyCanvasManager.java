package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public final class SkyCanvasManager {

    private final StarCatalogue catalogue;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    private final Canvas canvas;
    private final SkyCanvasPainter painter;
    private final ObjectBinding<StereographicProjection> projection;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> observedSky;

    private final ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    public final DoubleBinding mouseAzDeg;
    public final DoubleBinding mouseAltDeg;
    public final ObjectBinding<CelestialObject> objectUnderMouse;

    private static final int MAX_DISTANCE = 10;
    private static final int AZ_MOVE = 10;
    private static final int ALT_MOVE = 5;
    private static final ClosedInterval FOV = ClosedInterval.of(30, 150);
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, 360);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);

    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
        this.catalogue = catalogue;
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        canvas = new Canvas();
        painter = new SkyCanvasPainter(canvas);

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double dilatation = dilatation(getProjection(), canvas.getWidth(), viewingParametersBean.getFieldOfViewDeg());
                    return Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth() / 2, canvas.getHeight() / 2);
                }, projection, canvas.widthProperty(), canvas.heightProperty(), viewingParametersBean.fieldOfViewDegProperty());

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), getProjection(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneIdProperty(), observerLocationBean.lonDegProperty(), observerLocationBean.latDegProperty(), projection);


        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    try {
                        Point2D inverse = getPlaneToCanvas().inverseTransform(getMousePosition().x(), getMousePosition().y());
                        return (getProjection().inverseApply(CartesianCoordinates.of(inverse.getX(), inverse.getY())));
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                }, projection, planeToCanvas, mousePosition);


        mouseAzDeg = Bindings.createDoubleBinding(
                () -> {
                    if (getMouseHorizontalPosition() == null) {
                        return 0d;
                    } else {
                        return getMouseHorizontalPosition().azDeg();
                    }
                }, mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> {
                    if (getMouseHorizontalPosition() == null) {
                        return 0d;
                    } else {
                        return getMouseHorizontalPosition().altDeg();
                    }
                }, mouseHorizontalPosition);

        objectUnderMouse = Bindings.createObjectBinding(
                () -> {
                    try {
                        if (getMouseHorizontalPosition() == null) {
                            return null;
                        } else {
                            return getObservedSky().objectClosestTo(
                                    getProjection().apply(getMouseHorizontalPosition()), getPlaneToCanvas().inverseDeltaTransform(MAX_DISTANCE, 0).magnitude()).orElse(null);
                        }
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                }, planeToCanvas, observedSky, mouseHorizontalPosition);

        canvas.setOnKeyPressed(e -> {
            double azCenter = viewingParametersBean.getCenter().azDeg();
            double altCenter = viewingParametersBean.getCenter().altDeg();

            if (e.getCode() == KeyCode.UP && ALT_INTERVAL.contains(altCenter + ALT_MOVE)) {
                altCenter += ALT_MOVE;
                System.out.println(altCenter);
            } else if (e.getCode() == KeyCode.DOWN && ALT_INTERVAL.contains(altCenter - ALT_MOVE)) {
                altCenter -= ALT_MOVE;
                System.out.println(altCenter);
            } else if (e.getCode() == KeyCode.RIGHT) {
                azCenter = AZ_INTERVAL.reduce(azCenter + AZ_MOVE);
            } else if (e.getCode() == KeyCode.LEFT) {
                azCenter = AZ_INTERVAL.reduce(azCenter - AZ_MOVE);
            }
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(azCenter, altCenter));
        });

        canvas.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                canvas.requestFocus();
            }
        });

        canvas.setOnMouseMoved(e -> setMousePosition(CartesianCoordinates.of(e.getX(), e.getY())));

        canvas.setOnScroll(e -> {
            double zoom = viewingParametersBean.getFieldOfViewDeg();
            if (Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY())) {
                zoom += e.getDeltaX();
            } else {
                zoom += e.getDeltaY();
            }
            viewingParametersBean.setFieldOfViewDeg(FOV.clip(zoom));

        });

        observedSky.addListener((p, o, n) -> paint());

        planeToCanvas.addListener((p, o, n) -> paint());
    }

    public Canvas canvas() {
        return canvas;
    }

    private void paint() {
        painter.clear();
        painter.drawStars(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawPlanets(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawSun(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawMoon(getObservedSky(), getProjection(), getPlaneToCanvas());
        painter.drawHorizon(getProjection(), getPlaneToCanvas());
    }

    private StereographicProjection getProjection() {
        return projection.getValue();
    }

    private Transform getPlaneToCanvas() {
        return planeToCanvas.getValue();
    }

    private ObservedSky getObservedSky() {
        return observedSky.getValue();
    }

    private CartesianCoordinates getMousePosition() {
        return mousePosition.get();
    }

    private void setMousePosition(CartesianCoordinates mousePosition) {
        this.mousePosition.set(mousePosition);
    }

    public HorizontalCoordinates getMouseHorizontalPosition() {
        return mouseHorizontalPosition.getValue();
    }

    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    public ObservableDoubleValue mouseAltProperty() {
        return mouseAltDeg;
    }

    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.getValue();
    }

    private double dilatation(StereographicProjection projection, double width, double fieldOfView) {
        return width / projection.applyToAngle(Angle.ofDeg(fieldOfView));
    }

}
