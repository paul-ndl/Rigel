package ch.epfl.rigel.bonus;

import ch.epfl.rigel.gui.Main;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Un menu
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public class Menu {

    private static final String TITLE = "Menu";
    private static final String BACKGROUND_URL = "background_stars.jpg";

    private static final String MAIN_LABEL = "RIGEL";
    private static final String FONT_LABEL = "Impact";
    private static final double SIZE_LABEL = 50;

    private static final String LOCATION_BUTTON = "CHOISIR LA LOCALISATION";
    private static final String SKY_BUTTON = "ACCÉDER À LA VUE DU CIEL DEPUIS LAUSANNE";
    private static final String ABOUT_BUTTON = "À PROPOS";
    private static final double BUTTON_WIDTH = 350;
    private static final double BUTTON_HEIGHT = 40;

    private static final double INIT_LON = 6.57;
    private static final double INIT_LAT = 46.52;

    /**
     * Construit un menu
     *
     * @param primaryStage la scène
     */
    public Menu(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-image: url('" + BACKGROUND_URL + "');-fx-background-size: stretch;-fx-background-position:center top;");

        Label headerLabel = new Label(MAIN_LABEL);
        headerLabel.setFont(Font.font(FONT_LABEL, FontWeight.BOLD, SIZE_LABEL));
        headerLabel.setTextFill(Color.WHITE);
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        Button location = new Button(LOCATION_BUTTON);
        location.setPrefHeight(BUTTON_HEIGHT);
        location.setPrefWidth(BUTTON_WIDTH);
        location.setStyle("-fx-text-fill: white; -fx-background-color: black");
        location.setOnMouseEntered(me -> location.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        location.setOnMouseExited(me -> location.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        location.setOnAction(e -> new Globe(primaryStage));
        gridPane.add(location, 0, 1);

        Button sky = new Button(SKY_BUTTON);
        sky.setPrefHeight(BUTTON_HEIGHT);
        sky.setPrefWidth(BUTTON_WIDTH);
        sky.setStyle("-fx-text-fill: white; -fx-background-color: black");
        sky.setOnMouseEntered(me -> sky.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        sky.setOnMouseExited(me -> sky.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        sky.setOnAction(me -> new Main(primaryStage, INIT_LON, INIT_LAT));
        gridPane.add(sky, 0, 2);


        Button info = new Button(ABOUT_BUTTON);
        info.setPrefHeight(BUTTON_HEIGHT);
        info.setPrefWidth(BUTTON_WIDTH);
        info.setStyle("-fx-text-fill: white; -fx-background-color: black");
        info.setOnMouseEntered(me -> info.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        info.setOnMouseExited(me -> info.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        info.setOnAction(me -> new About(primaryStage));
        gridPane.add(info, 0, 3);

        Scene scene = new Scene(gridPane, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);

    }


}
