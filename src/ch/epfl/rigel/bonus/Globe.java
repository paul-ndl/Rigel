package ch.epfl.rigel.bonus;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class Globe {

    private Stage primaryStage;

    public Globe(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Earth earth = new Earth();
        Pane pane = earth.getPane();

        HBox location = location(pane, earth.getEarth());
        BorderPane main = new BorderPane(pane, null, location, null, null);

        Scene scene = new Scene(main, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setTitle("Globe Terrestre");
        primaryStage.setScene(scene);
    }

    private HBox location(Pane pane , Sphere meshView){

        HBox coordinates = new HBox();
        coordinates.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left; -fx-spacing: 10; -fx-padding: 4;");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //longitude info
        Label lonLabel = new Label("Longitude (°) : ");
        TextField lonField = new TextField();
        TextFormatter<Number> lonTextFormatter = lonTextFormatter();
        lonField.setTextFormatter(lonTextFormatter);
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        gridPane.add(lonLabel, 0, 1);
        gridPane.add(lonField, 0, 2);
        //latitude info
        Label latLabel = new Label("Latitude (°) : ");
        TextField latField = new TextField();
        TextFormatter<Number> latTextFormatter = latTextFormatter();
        latField.setTextFormatter(latTextFormatter);
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        gridPane.add(latLabel, 0, 3);
        gridPane.add(latField, 0, 4);
        //select button
        Button select = new Button("ACCÉDER AU CIEL");
        select.setPrefHeight(40);
        select.setPrefWidth(150);
        select.setStyle("-fx-text-fill: white; -fx-background-color: black");
        select.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                select.setStyle("-fx-text-fill: white; -fx-background-color: red");;
            }
        });
        select.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                select.setStyle("-fx-text-fill: white; -fx-background-color: black");;
            }
        });
        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try{
                    new Main2(primaryStage, (Double) lonTextFormatter.getValue(), (Double) latTextFormatter.getValue());
                } catch (IOException exception){

                }
            }
        });
        gridPane.add(select, 0, 5);
        //choose point on earth
        meshView.setOnMousePressed(e ->
        {
            if(e.isSecondaryButtonDown()){
                Point3D point = cartesianToPoint3D(e.getSceneX(), e.getSceneY(), pane, -Angle.ofDeg(meshView.getRotate()));
                GeographicCoordinates coord = Point3DToGeoCoord(point.getX(), point.getY(), point.getZ());
                lonTextFormatter.setValue(coord.lonDeg());
                latTextFormatter.setValue(coord.latDeg());
            }
        });

        coordinates.getChildren().addAll(gridPane);
        return coordinates;
    }

    private Point3D cartesianToPoint3D(double x, double y, Pane pane, double angle){
        //rescale the cartesian coordinates
        double scaleX = pane.getWidth()/2;
        double scaleY = pane.getHeight()/2;
        double factor = pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2;
        double xA = (x - scaleX)/factor;
        double yA = (y - scaleY)/factor;
        //transform in 3D
        double xB = xA;
        double yB = yA;
        double zB = -Math.sqrt(1-xA*xA-yA*yA);
        //add rotation
        double xC = Math.cos(angle)*xB + Math.sin(angle)*zB;
        double yC = yB;
        double zC = -Math.sin(angle)*xB + Math.cos(angle)*zB;
        //return 3D point
        Point3D point = new Point3D(xC, yC, zC);
        return point;
    }

    private GeographicCoordinates Point3DToGeoCoord(double x, double y, double z){
        //latitude calculation
        double lat = Angle.toDeg(Math.asin(-y)) + 0.7;
        //longitude calculation
        double lonA = Angle.toDeg(Math.asin(-x/Math.cos(Angle.ofDeg(lat-0.2))));
        double lonB = Angle.toDeg(Math.acos(z/Math.cos(Angle.ofDeg(lat-0.2))));
        double lon;
        if(z>0){
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

    private TextFormatter<Number> lonTextFormatter(){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newLonDeg = stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg) && newLonDeg!=0 ? change : null;
            } catch (Exception e) {
                return null;
            }
        });
        return new TextFormatter<>(stringConverter, 6.57, lonFilter);
    }

    private TextFormatter<Number> latTextFormatter(){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newLatDeg = stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLatDeg) && newLatDeg!=0 ? change : null;
            } catch (Exception e) {
                return null;
            }
        });
        return new TextFormatter<>(stringConverter, 46.52, lonFilter);
    }
}