package ch.epfl.rigel.bonus;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.net.URL;

public final class Earth {

    private Pane pane;
    private MeshView meshView;

    public Earth(){
        ObjModelImporter objImporter = new ObjModelImporter();
        try{
            URL modelUrl = this.getClass().getResource("/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e){

        }
        MeshView[] meshViews = objImporter.getImport();
        meshView = meshViews[0];

        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/earth_texture.png").toExternalForm()));
        meshView.setMaterial(texture);
        meshView.setCursor(Cursor.CROSSHAIR);

        Group root = new Group(meshViews[0]);
        pane = new Pane(root);
        pane.setStyle("-fx-background-color : black;");

        meshView.scaleXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshView.scaleYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshView.scaleZProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()>pane.getHeight() ? pane.getHeight()/2 : pane.getWidth()/2,
                pane.heightProperty(), pane.widthProperty()
        ));
        meshView.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getWidth()/2,
                pane.widthProperty()
        ));
        meshView.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> pane.getHeight()/2,
                pane.heightProperty()
        ));


        meshView.setRotationAxis(Rotate.Y_AXIS);
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                meshViews[0].setRotate(meshViews[0].getRotate() - 5);
            } else if (e.getCode() == KeyCode.LEFT) {
                meshViews[0].setRotate(meshViews[0].getRotate() + 5);
            }
        });

        pane.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                pane.requestFocus();
            }
        });

    }

    public Pane getPane() {
        return pane;
    }

    public MeshView getEarth(){
        return meshView;
    }
}
