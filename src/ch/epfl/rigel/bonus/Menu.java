package ch.epfl.rigel.bonus;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Menu extends Application {

    ObjectBinding<Transform> transform;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        Group root3D = new Group();
        Pane pane3D = new Pane(root3D);

        Sphere world = world(primaryStage);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setSpecularColor(Color.ORANGE);
        redMaterial.setDiffuseColor(Color.RED);
        List<Point3D> cities = CityLoader.geoCoordTo3dCoord();
        for(Point3D point : cities){
            Sphere s = new Sphere(0.015);
            s.setMaterial(redMaterial);
            System.out.println(point);
            s.setTranslateX(point.getX());
            s.setTranslateY(point.getY());
            s.setTranslateZ(point.getZ());
            pane3D.getChildren().add(s);
        }

        pane3D.getChildren().add(world);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Create scene
        Scene scene = new Scene(pane3D, 600, 600, true);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);

        //Add the scene to the stage and show it
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private Sphere world(Stage primaryStage){
        Sphere world = new Sphere();
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/earth_texture.png").toExternalForm()));
        world.setMaterial(texture);
        return world;
    }

    private List<Sphere> city(Pane worldPane){
        List<Sphere> cities = new ArrayList<>();
        return cities;
    }

    private BorderPane location(){
        Text city = new Text();
        city.textProperty().bind(Bindings.format(Locale.ROOT, "Ville : "));
        BorderPane location = new BorderPane(city, null, null, null, null);
        location.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return location;
    }


}