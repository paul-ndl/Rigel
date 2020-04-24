package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;

@FunctionalInterface
public interface TimeAccelerator {

    ZonedDateTime adjust(ZonedDateTime initSimuTime, long elapsedRealTime);

    static TimeAccelerator continuous(int alpha){
        return (ZonedDateTime initSimuTime, long elapsedRealTime) ->
                initSimuTime.plusNanos(alpha*elapsedRealTime);
    }

    static TimeAccelerator discrete(int f, Duration S){
        return (ZonedDateTime initSimuTime, long elapsedRealTime) ->
                initSimuTime.plusNanos((long) Math.floor(f*elapsedRealTime)*S.getNano());
    }

}
