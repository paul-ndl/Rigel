package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Un bean contenant les paramètres déterminant la portion du ciel visible
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class ViewingParametersBean {

    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>(null);

    /**
     * Retourne la propriété du champ de vue
     *
     * @return la propriété du champ de vue
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Retourne le champ de vue
     *
     * @return le champ de vue
     */
    public Double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * Paramètre le champ de vue à celui donné
     *
     * @param fieldOfViewDeg le nouveau champ de vue
     */
    public void setFieldOfViewDeg(Double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * Retourne la propriété du centre de vue
     *
     * @return la propriété du centre de vue
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Retourne le centre de vue
     *
     * @return le centre de vue
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Paramètre le centre de vue à celui donné
     *
     * @param center le nouveau centre de vue
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
