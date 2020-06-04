package ch.epfl.rigel.bonus;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Un "à propos"
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class About {

    private static final String TITLE = "A propos";
    private static final String MENU_BUTTON = "RETOURNER AU MENU";
    private static final double BUTTON_WIDTH = 150;
    private static final double BUTTON_HEIGHT = 40;

    /**
     * Construit un "à propos"
     *
     * @param primaryStage la scène
     */
    public About(Stage primaryStage) {
        Text text = new Text("Zouzou");

        HBox menu = new HBox();
        menu.setStyle("-fx-alignment: baseline-center; -fx-spacing: 10; -fx-padding: 4;");

        Button menuButton = new Button(MENU_BUTTON);
        menuButton.setPrefHeight(BUTTON_HEIGHT);
        menuButton.setPrefWidth(BUTTON_WIDTH);
        menuButton.setStyle("-fx-text-fill: white; -fx-background-color: black");
        menuButton.setOnMouseEntered(me -> menuButton.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        menuButton.setOnMouseExited(me -> menuButton.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        menuButton.setOnAction(e -> new Menu(primaryStage));

        menu.getChildren().add(menuButton);

        BorderPane pane = new BorderPane(text, null, null, menu, null);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
    }


}
