package ch.epfl.rigel.bonus;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {


    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-image: url('background_stars.jpg');-fx-background-size: stretch;-fx-background-position:center top;");

        Label headerLabel = new Label("RIGEL");
        headerLabel.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        headerLabel.setTextFill(Color.WHITE);
        gridPane.add(headerLabel, 0,0,2,1);
        gridPane.setHalignment(headerLabel, HPos.CENTER);
        gridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Button location = new Button("CHOISIR LA LOCALISATION");
        location.setPrefHeight(40);
        location.setPrefWidth(350);
        location.setStyle("-fx-text-fill: white; -fx-background-color: black");
        location.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                location.setStyle("-fx-text-fill: white; -fx-background-color: red");;
            }
        });
        location.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                location.setStyle("-fx-text-fill: white; -fx-background-color: black");;
            }
        });
        location.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                new Globe(primaryStage);
            }
        });
        gridPane.add(location, 0,1);

        Button sky = new Button("ACCÉDER À LA VUE DU CIEL DEPUIS LAUSANNE");
        sky.setPrefHeight(40);
        sky.setPrefWidth(350);
        sky.setStyle("-fx-text-fill: white; -fx-background-color: black");
        sky.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                sky.setStyle("-fx-text-fill: white; -fx-background-color: red");;
            }
        });
        sky.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                sky.setStyle("-fx-text-fill: white; -fx-background-color: black");;
            }
        });
        sky.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try{
                    new Main2(primaryStage,6.57, 46.52);
                } catch (IOException exception){

                }
            }
        });
        gridPane.add(sky, 0,2);


        Button info = new Button("À PROPOS");
        info.setPrefHeight(40);
        info.setPrefWidth(350);
        info.setStyle("-fx-text-fill: white; -fx-background-color: black");
        info.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                info.setStyle("-fx-text-fill: white; -fx-background-color: red");;
            }
        });
        info.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){
                info.setStyle("-fx-text-fill: white; -fx-background-color: black");;
            }
        });
        gridPane.add(info, 0,3);



        primaryStage.setTitle("Menu");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();
    }


}
