package ch.epfl.rigel.bonus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Instruction {

    public Instruction(Stage stage) {
        stage.setTitle("Instructions");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setStyle("-fx-background-color: #C1C1C1;");
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label label1 = new Label("1. Faîtes pivoter le globe à l'aide des touches DROITE/GAUCHE du clavier");
        gridPane.add(label1, 0, 1);
        Label label2 = new Label("2. Choisissez un point sur le globe à l'aide du clic gauche de la souris");
        gridPane.add(label2, 0, 2);
        Label label3 = new Label("3. Accédez au ciel à la position choisie à l'aide du bouton dédié");
        gridPane.add(label3, 0, 3);

        Button button = new Button("OK");
        gridPane.add(button, 1, 4);

        button.setOnAction(e -> stage.close());

        Scene scene = new Scene(gridPane, 430, 150);

        stage.setHeight(150);
        stage.setMaxHeight(150);
        stage.setMinHeight(150);
        stage.setWidth(430);
        stage.setMaxWidth(430);
        stage.setMinWidth(430);
        stage.setScene(scene);
        stage.show();
    }
}
