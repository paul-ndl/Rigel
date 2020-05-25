package ch.epfl.rigel.bonus2;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;

import java.net.URL;

public final class Earth2 {

    private Sphere world;
    private Pane pane = new Pane();

    public Earth2(){
        world = new Sphere(1);
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/test5.jpg").toExternalForm()));
        world.setMaterial(texture);

        ObjModelImporter objImporter = new ObjModelImporter();
        try{
            URL modelUrl = this.getClass().getResource("/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e){

        }
        MeshView[] meshViews = objImporter.getImport();
        meshViews[0].setMaterial(texture);



        pane.getChildren().add(world);
    }

    public Pane getPane(){
        return pane;
    }

}
