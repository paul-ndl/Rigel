package ch.epfl.rigel.bonus;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.gui.Main;
import ch.epfl.rigel.math.Angle;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;

/**
 * Un globe terrestre
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public class Globe {

    private final Stage primaryStage;

    private static final String TITLE = "Globe Terrestre";
    private static final double INIT_LON = 6.57;
    private static final double INIT_LAT = 46.52;
    private static final String SKY_BUTTON = "ACCÉDER AU CIEL";
    private static final String MENU_BUTTON = "RETOURNER AU MENU";
    private static final double BUTTON_WIDTH = 150;
    private static final double BUTTON_HEIGHT = 40;

    /**
     * Construit un globe terrestre
     *
     * @param primaryStage la scène
     */
    public Globe(Stage primaryStage) {

        this.primaryStage = primaryStage;
        Earth earth = new Earth();
        Pane pane = earth.getPane();

        HBox location = location(pane, earth.getEarth());
        BorderPane main = new BorderPane(pane, null, location, null, null);

        Scene scene = new Scene(main, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
    }

    private HBox location(Pane pane, Sphere meshView) {

        HBox coordinates = new HBox();
        coordinates.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left; -fx-spacing: 10; -fx-padding: 4;");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        //Information sur la longitude
        Label lonLabel = new Label("Longitude (°) : ");
        TextField lonField = new TextField();
        TextFormatter<Number> lonTextFormatter = coordTextFormatter(true, INIT_LON);
        lonField.setTextFormatter(lonTextFormatter);
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        gridPane.add(lonLabel, 0, 1);
        gridPane.add(lonField, 0, 2);

        //Information sur la latitude
        Label latLabel = new Label("Latitude (°) : ");
        TextField latField = new TextField();
        TextFormatter<Number> latTextFormatter = coordTextFormatter(false, INIT_LAT);
        latField.setTextFormatter(latTextFormatter);
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        gridPane.add(latLabel, 0, 3);
        gridPane.add(latField, 0, 4);

        //Bouton de sélection
        Button select = new Button(SKY_BUTTON);
        select.setPrefHeight(BUTTON_HEIGHT);
        select.setPrefWidth(BUTTON_WIDTH);
        select.setStyle("-fx-text-fill: white; -fx-background-color: black");
        select.setOnMouseEntered(me -> select.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        select.setOnMouseExited(me -> select.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        select.setOnAction(e -> {
            try {
                new Main(primaryStage, (Double) lonTextFormatter.getValue(), (Double) latTextFormatter.getValue());
            } catch (IOException ignored) {

            }
        });
        gridPane.add(select, 0, 5);

        //Bouton de retour au menu
        Button menu = new Button(MENU_BUTTON);
        menu.setPrefHeight(BUTTON_HEIGHT);
        menu.setPrefWidth(BUTTON_WIDTH);
        menu.setStyle("-fx-text-fill: white; -fx-background-color: black");
        menu.setOnMouseEntered(me -> menu.setStyle("-fx-text-fill: white; -fx-background-color: red"));
        menu.setOnMouseExited(me -> menu.setStyle("-fx-text-fill: white; -fx-background-color: black"));
        menu.setOnAction(me -> new Menu(primaryStage));
        gridPane.add(menu, 0, 6);

        //Information sur la rotation
        Label rotation = new Label("(Utilisez les flèches directionnelles");
        Label rotation2 = new Label("pour faire pivoter  le globe)");
        gridPane.add(rotation, 0, 7);
        gridPane.add(rotation2, 0, 8);

        //Choix du point sur le globe
        meshView.setOnMousePressed(e ->
        {
            if (e.isSecondaryButtonDown()) {
                Point3D point = cartesianToPoint3D(e.getSceneX(), e.getSceneY(), pane, -Angle.ofDeg(meshView.getRotate()));
                GeographicCoordinates coord = Point3DToGeoCoord(point.getX(), point.getY(), point.getZ());
                lonTextFormatter.setValue(coord.lonDeg());
                latTextFormatter.setValue(coord.latDeg());
            }
        });

        coordinates.getChildren().addAll(gridPane);
        return coordinates;
    }

    private Point3D cartesianToPoint3D(double x, double y, Pane pane, double angle) {
        //Ajustement des coordonnées cartésiennes
        double scaleX = pane.getWidth() / 2;
        double scaleY = pane.getHeight() / 2;
        double factor = pane.getWidth() > pane.getHeight() ? pane.getHeight() / 2 : pane.getWidth() / 2;
        double xA = (x - scaleX) / factor;
        double yA = (y - scaleY) / factor;
        //Transformation en 3D
        double xB = xA;
        double yB = yA;
        double zB = -Math.sqrt(1 - xA * xA - yA * yA);
        //Ajout de la matrice de rotation
        double xC = Math.cos(angle) * xB + Math.sin(angle) * zB;
        double yC = yB;
        double zC = -Math.sin(angle) * xB + Math.cos(angle) * zB;
        //Retourne les coordonnées 3D
        Point3D point = new Point3D(xC, yC, zC);
        return point;
    }

    private GeographicCoordinates Point3DToGeoCoord(double x, double y, double z) {
        //Calcul de la latitude
        double lat = Angle.toDeg(Math.asin(-y));
        //Calcul de la longitude
        double lonA = Angle.toDeg(Math.asin(-x / Math.cos(Angle.ofDeg(lat))));
        double lonB = Angle.toDeg(Math.acos(z / Math.cos(Angle.ofDeg(lat))));
        double lon;
        if (z > 0) {
            lon = lonA;
        } else {
            if (x < 0) {
                lon = lonB;
            } else {
                lon = -lonB;
            }
        }
        return GeographicCoordinates.ofDeg(lon, lat);
    }

    private TextFormatter<Number> coordTextFormatter(boolean lon, double defaultValue) {
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newDeg = stringConverter.fromString(newText).doubleValue();
                if (lon) {
                    return GeographicCoordinates.isValidLonDeg(newDeg) ? change : null;
                } else {
                    return GeographicCoordinates.isValidLatDeg(newDeg) ? change : null;
                }
            } catch (Exception e) {
                return null;
            }
        });
        return new TextFormatter<>(stringConverter, defaultValue, filter);
    }
}