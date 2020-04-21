package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator = null;
    private SimpleBooleanProperty running = null;

    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean = dateTimeBean;
    }

    private ObjectProperty<TimeAccelerator> acceleratorProperty(){
        return accelerator;
    }

    private TimeAccelerator getAccelerator(){
        return accelerator.get();
    }

    private void setAccelerator(TimeAccelerator accelerator){
        this.accelerator.set(accelerator);
    }

    private ReadOnlyBooleanProperty runningProperty(){
        return running;
    }

    private Boolean getRunning(){
        return running.get();
    }

    private void setRunning(Boolean running){
        this.running.set(running);
    }

    @Override
    public void handle(long now) {
        getAccelerator().adjust(dateTimeBean.getZoneDateTime(), now);
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
