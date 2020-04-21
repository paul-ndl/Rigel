package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean dateTimeBean;
    private SimpleObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private SimpleBooleanProperty running = new SimpleBooleanProperty(false);

    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
    }

    public ObjectProperty<TimeAccelerator> acceleratorProperty(){
        return accelerator;
    }

    public TimeAccelerator getAccelerator(){
        return accelerator.get();
    }

    public void setAccelerator(TimeAccelerator accelerator){
        this.accelerator.set(accelerator);
    }

    public ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    public Boolean getRunning(){
        return running.get();
    }

    public void setRunning(Boolean running){
        this.running.set(running);
    }

    @Override
    public void handle(long now) {
        getAccelerator().adjust(dateTimeBean.getZonedDateTime(), now);
    }

    @Override
    public void start(){
        super.start();
        setRunning(true);
    }

    @Override
    public void stop(){
        super.stop();
        setRunning(false);
    }
}
