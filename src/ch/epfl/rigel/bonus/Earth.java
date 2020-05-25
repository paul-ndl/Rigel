package ch.epfl.rigel.bonus;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public final class Earth {

    private Pane pane;
    private MeshView meshView;
    private Sphere world;

    public Earth(){
        world = new Sphere();
        PhongMaterial texture1 = new PhongMaterial();
        texture1.setDiffuseMap(new Image(getClass().getResource("/test11.png").toExternalForm()));
        world.setMaterial(texture1);
        world.setCursor(Cursor.CROSSHAIR);

        Group root = new Group(world);
        pane = new Pane(root);
        pane.setStyle("-fx-background-image: url('ciel_etoile.jpg');-fx-background-size: stretch;-fx-background-position:center top;");

        pane.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                pane.requestFocus();
            }
        });

        world.radiusProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        world.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()/2,
                pane.widthProperty()
        ));
        world.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getHeight()/2,
                pane.heightProperty()
        ));


        world.setRotationAxis(Rotate.Y_AXIS);
        world.setRotate(180);
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                world.setRotate(world.getRotate() - 5);
            } else if (e.getCode() == KeyCode.LEFT) {
                world.setRotate(world.getRotate() + 5);
            }
            e.consume();
        });

    }

    public Pane getPane() {
        return pane;
    }

    public Sphere getEarth(){
        return world;
    }
}
