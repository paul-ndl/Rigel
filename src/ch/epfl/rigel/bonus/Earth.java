package ch.epfl.rigel.bonus;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;

import java.net.URL;
import java.util.List;

public final class Earth {

    private Sphere world;
    private Pane pane = new Pane();

    public Earth(){
        world = new Sphere(1);
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/earth_texture.png").toExternalForm()));
        world.setMaterial(texture);

        ObjModelImporter objImporter = new ObjModelImporter();
        try{
            URL modelUrl = this.getClass().getResource("/earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e){

        }
        MeshView[] meshViews = objImporter.getImport();
        meshViews[0].setMaterial(texture);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        List<Point3D> cities = List.copyOf(CityLoader.CITIES_MAP.values());
        for(Point3D point : cities){
            Sphere s = new Sphere(0.01);
            s.setMaterial(redMaterial);
            s.setTranslateX(point.getX());
            s.setTranslateY(point.getY());
            s.setTranslateZ(point.getZ());
            pane.getChildren().add(s);
        }


        pane.getChildren().add(meshViews[0]);
    }

    public Pane getPane(){
        return pane;
    }

}
