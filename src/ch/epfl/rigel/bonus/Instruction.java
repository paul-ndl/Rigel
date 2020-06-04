package ch.epfl.rigel.bonus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Un popup d'instructions
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public class Instruction {

    private static final String TITLE = "Instructions";
    private static final Image ICON = new Image("/instruction.ico");
    private static final double POPUP_WIDTH = 450;
    private static final double POPUP_HEIGHT = 150;

    /**
     * Construit un popup d'instructions
     *
     * @param stage la scène
     */
    public Instruction(Stage stage) {
        stage.setTitle(TITLE);
        stage.getIcons().add(ICON);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label label1 = new Label("1. Faîtes pivoter le globe à l'aide des touches DROITE/GAUCHE du clavier");
        label1.setTextFill(Color.WHITE);
        gridPane.add(label1, 0, 1);
        Label label2 = new Label("2. Choisissez un point sur le globe à l'aide du clic gauche de la souris");
        label2.setTextFill(Color.WHITE);
        gridPane.add(label2, 0, 2);
        Label label3 = new Label("3. Accédez au ciel à la position choisie à l'aide du bouton dédié");
        label3.setTextFill(Color.WHITE);
        gridPane.add(label3, 0, 3);

        Button button = new Button("OK");
        gridPane.add(button, 1, 4);

        button.setOnAction(e -> stage.close());

        Scene scene = new Scene(gridPane, POPUP_WIDTH, POPUP_HEIGHT);

        stage.setHeight(POPUP_HEIGHT);
        stage.setMaxHeight(POPUP_HEIGHT);
        stage.setMinHeight(POPUP_HEIGHT);
        stage.setWidth(POPUP_WIDTH);
        stage.setMaxWidth(POPUP_WIDTH);
        stage.setMinWidth(POPUP_WIDTH);
        stage.setScene(scene);
        stage.show();
    }
}
