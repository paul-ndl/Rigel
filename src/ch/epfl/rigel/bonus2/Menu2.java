package ch.epfl.rigel.bonus2;


import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;

public class Menu2 extends Application {

    private Scene scene;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        Earth2 earth = new Earth2();
        Pane pane = earth.getPane();

        PerspectiveCamera camera = new PerspectiveCamera(true);
        CameraManager2 manager = new CameraManager2(camera, pane);

        // Create scene
        scene = new Scene(pane, 600, 600);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);

        //Add the scene to the stage and show it
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public Scene getScene(){
        return scene;
    }


    private BorderPane location(){
        Text city = new Text();
        city.textProperty().bind(Bindings.format(Locale.ROOT, "Ville : "));
        BorderPane location = new BorderPane(city, null, null, null, null);
        location.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return location;
    }

    private Point3D transformMouse(double x, double y, Pane pane, double angle){
        double scaleX = pane.getWidth()/2;
        double scaleY = pane.getHeight()/2;
        double xA = (x - scaleX)/288;
        double yA = (y - scaleY)/288;
        double xB = xA;
        double yB = yA;
        double zB = Math.cos(Math.asin(yA))*Math.sin(Math.acos(xA/(2*Math.asin(yA))));
        double xC = Math.cos(angle)*xB + Math.sin(angle)*zB;
        double yC = yB;
        double zC = -(-Math.sin(angle)*xB + Math.cos(angle)*zB);
        return new Point3D(xB, yB, zB);
    }

    private HorizontalCoordinates transformPoint(double x, double y){
        double lat = Angle.toDeg(Math.asin(-y)) + 0.2;
        double lon = Angle.toDeg(Math.asin(-x/Math.cos(lat))) - 2.8;
        return HorizontalCoordinates.ofDeg(lon, lat);
    }


}