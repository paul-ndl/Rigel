package ch.epfl.rigel.bonus;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * Une Terre
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Earth {

    private final Pane pane;
    private final Sphere world = new Sphere();

    private static final int INIT_ROTATION = 180;
    private static final int ROTATION_MOVE = 5;
    private static final String EARTH_TEXTURE = "/earth_texture.png";
    private static final String BACKGROUND_URL = "/stars_sky.jpg";

    /**
     * Construit une Terre
     */
    public Earth() {
        Group root = new Group(world);
        pane = new Pane(root);

        //Paramètre la taille et la position de la Terre
        world.radiusProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth() > pane.getHeight() ? pane.getHeight() / 2 : pane.getWidth() / 2,
                pane.heightProperty(), pane.widthProperty()
        ));
        world.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth() / 2,
                pane.widthProperty()
        ));
        world.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getHeight() / 2,
                pane.heightProperty()
        ));

        //Paramètre la rotation de la Terre
        world.setRotationAxis(Rotate.Y_AXIS);
        world.setRotate(INIT_ROTATION);
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                world.setRotate(world.getRotate() - ROTATION_MOVE);
            } else if (e.getCode() == KeyCode.LEFT) {
                world.setRotate(world.getRotate() + ROTATION_MOVE);
            }
            e.consume();
        });

        //Paramètre le curseur de la souris
        world.setCursor(Cursor.CROSSHAIR);

        //Applique la texture
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource(EARTH_TEXTURE).toExternalForm()));
        world.setMaterial(texture);

        pane.setStyle("-fx-background-image: url('" + BACKGROUND_URL + "');-fx-background-size: stretch;-fx-background-position:center top;");
        pane.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                pane.requestFocus();
            }
        });

    }

    /**
     * Retourne le panneau
     *
     * @return le panneau
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * Retourne la Terre
     *
     * @return la Terre
     */
    public Sphere getEarth() {
        return world;
    }
}
