package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public final class ObserverLocationBean {

    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    private final DoubleProperty latDeg = new SimpleDoubleProperty();
    private final ObservableValue<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(getLonDeg(), getLatDeg()),
            lonDeg, latDeg);

    public DoubleProperty lonDegProperty(){
        return lonDeg;
    }

    public Double getLonDeg(){
        return lonDeg.get();
    }

    public void setLonDeg(Double lonDeg){
        this.lonDeg.set(lonDeg);
    }

    public DoubleProperty latDegProperty(){
        return latDeg;
    }

    public Double getLatDeg(){
        return latDeg.get();
    }

    public void setLatDeg(Double latDeg){
        this.latDeg.set(latDeg);
    }

    public GeographicCoordinates getCoordinates(){
        return coordinates.getValue();
    }

    public void setCoordinates(GeographicCoordinates coordinates){
        setLonDeg(coordinates.lonDeg());
        setLatDeg(coordinates.latDeg());
    }



}
