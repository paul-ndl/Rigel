package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Un bean contenant les paramètres de la position de l'observateur
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class ObserverLocationBean {

    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    private final DoubleProperty latDeg = new SimpleDoubleProperty();
    private final ObjectBinding<GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(getLonDeg(), getLatDeg()),
            lonDeg, latDeg);

    /**
     * Retourne la propriété de la longitude de l'observateur
     *
     * @return la propriété de la longitude de l'observateur
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * Retourne la longitude de l'observateur
     *
     * @return la longitude de l'observateur
     */
    public Double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Paramètre la longitude de l'observateur à celle donnée
     *
     * @param lonDeg la nouvelle longitude en degrés
     */
    public void setLonDeg(Double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     * Retourne la propriété de la latitude de l'observateur
     *
     * @return la propriété de la latitude de l'observateur
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * Retourne la latitude de l'observateur
     *
     * @return la latitude de l'observateur
     */
    public Double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Paramètre la latitude de l'observateur à celle donnée
     *
     * @param latDeg la nouvelle latitude en degrés
     */
    public void setLatDeg(Double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * Retourne la propriété de la position de l'observateur
     *
     * @return la propriété de la position de l'observateur
     */
    public ObjectBinding<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Retourne la position de l'observateur
     *
     * @return la position de l'observateur
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }

    /**
     * Paramètre la position de l'observateur à celle donnée
     *
     * @param coordinates la nouvelle position
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        setLonDeg(coordinates.lonDeg());
        setLatDeg(coordinates.latDeg());
    }


}
