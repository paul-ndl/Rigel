package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean dateTimeBean;
    private final ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private final BooleanProperty running = new SimpleBooleanProperty(false);

    private ZonedDateTime initTime;
    private long time0;
    private boolean initial;

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
        if(initial){
            time0 = now;
            initial = false;
        }
        dateTimeBean.setZonedDateTime(getAccelerator().adjust(initTime, now-time0));
    }

    @Override
    public void start(){
        super.start();
        setRunning(true);
        initTime = dateTimeBean.getZonedDateTime();
        initial = true;
    }

    @Override
    public void stop(){
        super.stop();
        setRunning(false);
    }
}
