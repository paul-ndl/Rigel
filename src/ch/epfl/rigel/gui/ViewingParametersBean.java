package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;


public final class ViewingParametersBean {

    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    public DoubleProperty fieldOfViewDegProperty(){
        return fieldOfViewDeg;
    }

    public Double getFieldOfViewDeg(){
        return fieldOfViewDeg.get();
    }

    public void setFieldOfViewDeg(Double fieldOfViewDeg){
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return center;
    }

    public HorizontalCoordinates getCenter(){
        return center.get();
    }

    public void setCenter(HorizontalCoordinates center){
        this.center.set(center);
    }
}
