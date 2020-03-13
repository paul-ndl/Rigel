package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

public final class Moon extends CelestialObject {

    private float phase;

    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase){
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = phase;
        Preconditions.checkInInterval(ClosedInterval.of(0,1), phase);
    }

    @Override
    public String info(){
        return String.format(Locale.ROOT,"%s (%.1f%s)", super.name(), phase*100, "%");
    }
}
