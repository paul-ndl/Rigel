package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.*;



public final class ViewingParametersBean {

    private final IntegerProperty fieldOfViewDeg = new SimpleIntegerProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    public IntegerProperty fieldOfViewDegProperty(){
        return fieldOfViewDeg;
    }

    public int getFieldOfViewDeg(){
        return fieldOfViewDeg.get();
    }

    public void setFieldOfViewDeg(int fieldOfViewDeg){
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
