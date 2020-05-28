package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

/**
 * Un animateur de temps
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean dateTimeBean;
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final BooleanProperty running = new SimpleBooleanProperty(false);

    private ZonedDateTime initTime;
    private long time0;
    private boolean initial;

    /**
     * Construit un animateur de temps à partir de l'instant d'observation donné
     *
     * @param dateTimeBean l'instant d'observation
     */
    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
    }

    /**
     * Retourne la propriété de l'accélérateur
     *
     * @return la propriété de l'accélérateur
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Retourne l'accélérateur
     *
     * @return l'accélérateur
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * Paramètre l'accélérateur à celui donné
     *
     * @param accelerator le nouvel accélérateur
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }

    /**
     * Retourne la propriété du booléen running
     *
     * @return la propriété du booléen running
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return running;
    }

    /**
     * Retourne le booléen running
     *
     * @return le booléen running
     */
    public Boolean getRunning() {
        return running.get();
    }

    private void setRunning(Boolean running) {
        this.running.set(running);
    }

    /**
     * Modifie l'instant d'observation à chaque séquence de l'animation
     *
     * @param now la durée de la séquence courante donnée en nanosecondes
     * @see AnimationTimer#handle(long)
     */
    @Override
    public void handle(long now) {
        if (initial) {
            time0 = now;
            initial = false;
        }
        dateTimeBean.setZonedDateTime(getAccelerator().adjust(initTime, now - time0));
    }

    /**
     * Lance l'animation
     *
     * @see AnimationTimer#start()
     */
    @Override
    public void start() {
        super.start();
        setRunning(true);
        initTime = dateTimeBean.getZonedDateTime();
        initial = true;
    }

    /**
     * Arrête l'animation
     *
     * @see AnimationTimer#stop()
     */
    @Override
    public void stop() {
        super.stop();
        setRunning(false);
    }
}
