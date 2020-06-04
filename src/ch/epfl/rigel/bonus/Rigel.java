package ch.epfl.rigel.bonus;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Rigel
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Rigel extends Application {

    private static final String TITLE = "Menu";
    private static final Image ICON = new Image("/icon.ico");
    private static final double MIN_WIDTH = 1200;
    private static final double MIN_HEIGHT = 800;
    private static final double MAX_WIDTH = Screen.getPrimary().getBounds().getWidth() - 80;
    private static final double MAX_HEIGHT = Screen.getPrimary().getBounds().getHeight() - 80;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Lance le programme complet
     *
     * @param primaryStage la scène
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(ICON);
        primaryStage.setWidth(MAX_WIDTH);
        primaryStage.setHeight(MAX_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMaxWidth(MAX_WIDTH);
        primaryStage.setMaxHeight(MAX_HEIGHT);

        new Menu(primaryStage);

        primaryStage.show();

    }
}
