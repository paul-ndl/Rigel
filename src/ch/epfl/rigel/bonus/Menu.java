package ch.epfl.rigel.bonus;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;

public class Menu extends Application {

    private static final double ROTATE_SECS   = 30;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        Earth earth = new Earth();
        Pane pane = earth.getPane();


        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane);

        // Create scene
        Scene scene = new Scene(pane, 600, 600, true);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);


        //Add the scene to the stage and show it
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private BorderPane location(){
        Text city = new Text();
        city.textProperty().bind(Bindings.format(Locale.ROOT, "Ville : "));
        BorderPane location = new BorderPane(city, null, null, null, null);
        location.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return location;
    }


}