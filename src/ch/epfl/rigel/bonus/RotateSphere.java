package ch.epfl.rigel.bonus;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class RotateSphere extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        Sphere world = world(primaryStage);

        Group root = new Group(world);

        primaryStage.setTitle("World");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        Scene scene= new Scene (root);
        scene.setFill(Color.BLACK);
        scene.setCamera(new PerspectiveCamera());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Sphere world(Stage primaryStage){
        Sphere world = new Sphere();
        world.radiusProperty().bind(Bindings.createDoubleBinding(() ->
                        primaryStage.getWidth()>primaryStage.getHeight() ? primaryStage.getHeight()*0.35 : primaryStage.getWidth()*0.35,
                primaryStage.widthProperty(), primaryStage.heightProperty()
        ));
        world.translateXProperty().bind(Bindings.createDoubleBinding(() ->
                primaryStage.getWidth()/2, primaryStage.widthProperty()));
        world.translateYProperty().bind(Bindings.createDoubleBinding(() ->
                primaryStage.getHeight()/2, primaryStage.heightProperty()));
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/earth_texture.png").toExternalForm()));
        world.setMaterial(texture);

        world.setRotationAxis(Rotate.Y_AXIS);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler -> {
            switch (eventHandler.getCode()) {
                //increase the world size if we press A
                case RIGHT:
                    world.rotateProperty().set(world.getRotate()+15);
                    break;
                //decrease the world size if we press B
                case LEFT:
                    world.rotateProperty().set(world.getRotate()-15);
                    break;
            }
        });
        return world;
    }
}
