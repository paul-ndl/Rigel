package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticToHorizontalCoordinatesTest {
    @Test
    void EcliptictoHorizontal(){
        EclipticCoordinates ecl = EclipticCoordinates.of(Angle.ofDeg(8),Angle.ofDeg(-70));
        EclipticToEquatorialConversion e = new EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0, 0, ZoneId.of("Europe/Paris")));
        EquatorialToHorizontalConversion equToHor= new EquatorialToHorizontalConversion(ZonedDateTime.of(2009,7,6,0,0,0,0, ZoneId.of("Europe/Paris")), GeographicCoordinates.ofDeg(2.5,45));
        Function<EclipticCoordinates,HorizontalCoordinates> ecltoHor= e.andThen(equToHor);
        //assertEquals(HorizontalCoordinates.ofDeg(0.0,0.0).toString(),ecltoHor.apply(ecl).toString());

    }

}
