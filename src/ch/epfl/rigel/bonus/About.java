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
        Text text = new Text("Bonjour," +
                "\n \n Ceci est un programme qui permet de visualiser les étoiles dans le ciel!" +
                "\n Naviguez entre les différents menus facilement grâce à l'interface intuitive de l'application." +
                "\n \n Vous pouvez tout d'abord : " +
                "\n \n     1. Choisir l'endroit d'où vous souhaitez voir le ciel grâce à un globe terrestre rotatif" +
                "\n     2. Choisir de regarder le ciel directement depuis Lausanne" +
                "\n \n Une fois l'endroit choisi, vous pouvez obeserver le ciel." +
                "\n \n Plusieurs options s'offrent à vous :" +
                "\n \n     1. Vous pouvez changer le champ de vue à l'aide de la molette de la souris" +
                "\n     2. Vous pouvez \"tourner la tête\" à l'aide des flèches directionnelles de votre clavier" +
                "\n     3. Vous pouvez changer directement le lieu d'observation avec la longitude et latitude" +
                "\n     4. Vous pouvez changer l'instant d'observation (date, heure, fuseau horaire)" +
                "\n     5. Vous pouvez animer le ciel à la vitesse voulue" +
                "\n \n En bas de l'écran, vous apercevez la position de la souris (azimut, hauteur), ainsi que l'objet céleste le plus proche de votre souris." +
                "\n \n Profitez bien du ciel ;) !!!");

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
        pane.setStyle("-fx-background-color: #707171;");

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
    }


}
