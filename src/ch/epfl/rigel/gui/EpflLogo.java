package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class EpflLogo extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        // Fond blanc
        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        /*// Texte EPFL rouge
        Color red = Color.RED;
        Color derived = red.deriveColor(1,1,1,1);
        ctx.setFont(Font.font("Helvetica", 300));
        ctx.setFill(derived);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.BASELINE);
        ctx.fillText("EPFL", 400, 250);

        // Trous dans le E et le F
        ctx.setFill(Color.WHITE);
        ctx.fillRect(50, 126, 30, 26);
        ctx.fillRect(450, 126, 30, 26);*/
        ctx.setStroke(Color.RED);
        double radius = 50;
        ctx.strokeOval(400-radius/2, 300-radius/2, radius, radius);
        ctx.setStroke(Color.BLUE);
        ctx.setLineWidth(2);
        ctx.beginPath();
        ctx.lineTo(400-radius/2, 300-radius/2);
        ctx.lineTo(400-radius/2, radius);
        ctx.stroke();



        primaryStage.setScene(new Scene(new BorderPane(canvas)));
        primaryStage.show();
    }
}
