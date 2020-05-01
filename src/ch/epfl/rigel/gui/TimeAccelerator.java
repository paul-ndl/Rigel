package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;

@FunctionalInterface
public interface TimeAccelerator {

    ZonedDateTime adjust(ZonedDateTime initTime, long elapsedRealTime);

    static TimeAccelerator continuous(int alpha){
        return (ZonedDateTime initTime, long elapsedRealTime) ->
                initTime.plusNanos(alpha*elapsedRealTime);
    }

    static TimeAccelerator discrete(int f, Duration S){
        return (ZonedDateTime initTime, long elapsedRealTime) ->
            initTime.plusNanos((long) Math.floor(f*elapsedRealTime*1e-9)*S.toNanos());
    }

}
