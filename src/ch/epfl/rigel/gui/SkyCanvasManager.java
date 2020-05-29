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
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * Un manager du ciel
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class SkyCanvasManager {

    private final Canvas canvas;
    private final SkyCanvasPainter painter;
    private final ObjectBinding<StereographicProjection> projection;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> observedSky;

    private final ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    private final DoubleBinding mouseAzDeg;
    private final DoubleBinding mouseAltDeg;
    private final ObjectBinding<CelestialObject> objectUnderMouse;

    private static final int MAX_DISTANCE = 10;
    private static final double AZ_MOVE = 10;
    private static final double ALT_MOVE = 5;
    private static final ClosedInterval FOV = ClosedInterval.of(30, 150);
    private static final RightOpenInterval AZ_INTERVAL = RightOpenInterval.of(0, 360);
    private static final ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);

    /**
     * Construit un manager du ciel
     *
     * @param catalogue             le catalogue d'étoiles et d'astérismes du ciel
     * @param dateTimeBean          l'instant d'observation
     * @param observerLocationBean  la position de l'observateur
     * @param viewingParametersBean la portion du ciel visible
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

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
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneIdProperty(), observerLocationBean.coordinatesProperty(), projection);

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

            if (e.getCode() == KeyCode.UP) {
                altCenter = ALT_INTERVAL.clip(altCenter + ALT_MOVE);
            } else if (e.getCode() == KeyCode.DOWN) {
                altCenter = ALT_INTERVAL.clip(altCenter - ALT_MOVE);
            } else if (e.getCode() == KeyCode.RIGHT) {
                azCenter = AZ_INTERVAL.reduce(azCenter + AZ_MOVE);
            } else if (e.getCode() == KeyCode.LEFT) {
                azCenter = AZ_INTERVAL.reduce(azCenter - AZ_MOVE);
            }
            e.consume();
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

    /**
     * Retourne le canvas sur lequel le ciel est dessiné
     *
     * @return le canvas sur lequel le ciel est dessiné
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * Retourne la propriété de l'azimut par rapport à la position de la souris sur le canvas
     *
     * @return la propriété de l'azimut par rapport à la position de la souris sur le canvas
     */
    public DoubleBinding mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Retourne la propriété de l'altitude par rapport à la position de la souris sur le canvas
     *
     * @return la propriété de l'altitude par rapport à la position de la souris sur le canvas
     */
    public DoubleBinding mouseAltProperty() {
        return mouseAltDeg;
    }

    /**
     * Retourne la propriété de l'objet céleste le plus proche de la position de la souris sur le canvas
     *
     * @return la propriété de l'objet céleste le plus proche de la position de la souris sur le canvas
     */
    public ObjectBinding<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * Retourne l'objet céleste le plus proche de la position de la souris sur le canvas
     *
     * @return l'objet céleste le plus proche de la position de la souris sur le canvas
     */
    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.getValue();
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

    private HorizontalCoordinates getMouseHorizontalPosition() {
        return mouseHorizontalPosition.getValue();
    }

    private double dilatation(StereographicProjection projection, double width, double fieldOfView) {
        return width / projection.applyToAngle(Angle.ofDeg(fieldOfView));
    }

}
