package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.SQLOutput;
import java.time.ZonedDateTime;

public final class TimeAnimator extends AnimationTimer {

    private DateTimeBean dateTimeBean;
    private SimpleObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(null);
    private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    private long time0;
    private long deltaTime = 0;

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
        long actualTime = now;
        deltaTime = actualTime - time0 - deltaTime;
        System.out.println(deltaTime);
        dateTimeBean.setZonedDateTime(getAccelerator().adjust(dateTimeBean.getZonedDateTime(), deltaTime));
    }

    @Override
    public void start(){
        super.start();
        setRunning(true);
        time0 = System.nanoTime();
    }

    @Override
    public void stop(){
        super.stop();
        setRunning(false);
    }
}
