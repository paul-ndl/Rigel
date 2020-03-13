package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.math.ClosedInterval;

public final class Star extends CelestialObject {

    private int hipparcosId;
    private String name;
    private EquatorialCoordinates equatorialPos;
    private float magnitude, colorIndex;

    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos, 0,magnitude);
        Preconditions.checkArgument(hipparcosId>0);
        Preconditions.checkInInterval(ClosedInterval.of(-0.5,5.5), colorIndex);
        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    public int hipparcosId(){
        return hipparcosId;
    }

    public int colorTemperature(){
        return (int) Math.floor(4600*(1/(0.92*colorIndex+0.67) + 1/(0.92*colorIndex+1.7)));
    }
}
