package ch.epfl.rigel.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
//import javafx.scene.shape.RectangleBuilder;
import javafx.stage.Stage;

/**
 * A sample that demonstrates various mouse and scroll events and their usage.
 * Click the circles and drag them across the screen. Scroll the whole screen.
 * All events are logged to the console.
 *
 * @see javafx.scene.Cursor
 * @see javafx.scene.input.MouseEvent
 * @see javafx.event.EventHandler
 */
public class test extends Application {
    {
    }

    //variables for storing initial position before drag of circle
    private double initX;
    private double initY;
    private Point2D dragAnchor;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 500,500));
        // create circle with method listed below: paramethers: name of the circle, color of the circle, radius
        final Circle circleSmall = createCircle("Blue circle", Color.DODGERBLUE, 25);
        circleSmall.setTranslateX(200);
        circleSmall.setTranslateY(80);
        // and a second, bigger circle
        //       final Circle circleSmall = createCircle("Orange circle", Color.CORAL, 40);
        // we can set mouse event to any node, also on the rectangle
        circleSmall.setOnMouseMoved(new EventHandler<MouseEvent>() {
                                        public void handle(MouseEvent me) {
                                            System.out.println("ok");
                                        }
                                    }
        );
        // show all the circle , rectangle and console
        root.getChildren().addAll(circleSmall);
    }

    private Circle createCircle(final String name, final Color color, int radius) {
        //create a circle with desired name,  color and radius
        final Circle circle = new Circle(radius, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(0, Color.rgb(250,250,255)),
                new Stop(1, color)
        }));
        //add a shadow effect
        circle.setEffect(new InnerShadow(7, color.darker().darker()));
        //change a cursor when it is over circle
        circle.setCursor(Cursor.HAND);
        //add a mouse listeners
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                System.out.println("ok");
                double dragX = me.getSceneX() - dragAnchor.getX();
                double dragY = me.getSceneY() - dragAnchor.getY();
                //calculate new position of the circle
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                //if new position do not exceeds borders of the rectangle, translate to this position
                if ((newXPosition>=circle.getRadius()) && (newXPosition<=500-circle.getRadius())) {
                    circle.setTranslateX(newXPosition);
                }
                if ((newYPosition>=circle.getRadius()) && (newYPosition<=300-circle.getRadius())){
                    circle.setTranslateY(newYPosition);
                }
            }
        });
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                circle.toFront();
            }
        });
        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            }
        });
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //when mouse is pressed, store initial position
                initX = circle.getTranslateX();
                initY = circle.getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
            }
        });
        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            }
        });

        return circle;
    }

    /*private void showOnConsole(String text) {
         //if there is 8 items in list, delete first log message, shift other logs and  add a new one to end position
         if (consoleObservableList.size()==8) {
            consoleObservableList.remove(0);
         }
         consoleObservableList.add(text);
    }*/

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX
     * application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}