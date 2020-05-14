package ch.epfl.rigel.bonus2;

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
import java.util.Map;

public final class Earth2 {

    private Sphere world;
    private Pane pane = new Pane();

    public Earth2(){
        world = new Sphere(150);
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
        Map<Point3D, City> cities = (CityLoader.CITIES_MAP);
        for(Point3D point : cities.keySet()){
            System.out.println(point);
            System.out.println(cities.get(point));
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
