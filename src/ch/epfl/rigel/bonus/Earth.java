package ch.epfl.rigel.bonus;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.List;

public final class Earth {

    private Sphere world;
    private Pane pane = new Pane();

    public Earth(){
        world = new Sphere(1);
        PhongMaterial texture = new PhongMaterial();
        texture.setDiffuseMap(new Image(getClass().getResource("/test2.png").toExternalForm()));
        world.setMaterial(texture);

        PhongMaterial redMaterial = new PhongMaterial();
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


        pane.getChildren().add(world);
    }

    public Pane getPane(){
        return pane;
    }

}
