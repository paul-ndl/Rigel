package ch.epfl.rigel.bonus;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.URL;

public class rotation extends Application {
    @Override
    public void start(Stage stage) {
        ObjModelImporter objImporter = new ObjModelImporter();
        try{
            URL modelUrl = this.getClass().getResource("/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e){

        }
        MeshView[] meshViews = objImporter.getImport();
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/earth_texture.png").toExternalForm()));
        meshViews[0].setMaterial(texture);

        Group root = new Group(meshViews[0]);
        Pane pane = new Pane(root);
        meshViews[0].scaleXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshViews[0].scaleYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshViews[0].scaleZProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshViews[0].translateXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()/2,
                pane.widthProperty()
        ));
        meshViews[0].translateYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getHeight()/2,
                pane.heightProperty()
        ));


        pane.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                pane.requestFocus();
            }
            if(e.isSecondaryButtonDown()){
                Point3D point = transformMouse(e.getSceneX(), e.getSceneY(), pane, -Angle.ofDeg(meshViews[0].getRotate()));
                System.out.println(transformPoint(point.getX(), point.getY()));
            }
        });

        meshViews[0].setRotationAxis(Rotate.Y_AXIS);

        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                meshViews[0].setRotate(meshViews[0].getRotate() - 10);
            } else if (e.getCode() == KeyCode.LEFT) {
                meshViews[0].setRotate(meshViews[0].getRotate() + 10);
            }
        });


        Scene scene = new Scene(pane, 600, 600);

        stage.setTitle("Choose Location");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    private Point3D transformMouse(double x, double y, Pane pane, double angle){
        double scaleX = pane.getWidth()/2;
        double scaleY = pane.getHeight()/2;
        double factor = pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2;

        double xA = (x - scaleX)/factor;
        double yA = (y - scaleY)/factor;

        double xB = xA;
        double yB = yA;
        double zB = -Math.sqrt(1-xA*xA-yA*yA);

        double xC = Math.cos(angle)*xB + Math.sin(angle)*zB;
        double yC = yB;
        double zC = -Math.sin(angle)*xB + Math.cos(angle)*zB;

        Point3D point = new Point3D(xC, yC, zC);
        System.out.println(point);
        return point;
    }

    private HorizontalCoordinates transformPoint(double x, double y){
        double lat = Angle.toDeg(Math.asin(-y)) + 0.2;
        System.out.println(lat);
        double lon = Angle.toDeg(Math.asin(-x/Math.cos(Angle.ofDeg(lat-0.2)))) - 2.8;
        System.out.println(lon);
        return HorizontalCoordinates.ofDeg(lon, lat);
    }
}