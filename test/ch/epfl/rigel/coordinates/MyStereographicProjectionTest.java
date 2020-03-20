package ch.epfl.rigelTest.coordinates;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MyStereographicProjectionTest {

    private final static double EPSILON = 1e-6;
    private final static double EPSILON2 = 1e-5;

    private final static HorizontalCoordinates center = HorizontalCoordinates.ofDeg(0, 0);

    private final static StereographicProjection stereographicProjection =
            new StereographicProjection (HorizontalCoordinates.ofDeg(23, 45));

    private final static StereographicProjection stereographicProjection0 =
            new StereographicProjection (center);

    private final static StereographicProjection stereographicProjection45 =
            new StereographicProjection (HorizontalCoordinates.ofDeg(45, 45));
    @Test
    void apply() {
        assertEquals(-0.1316524976, stereographicProjection45.apply(HorizontalCoordinates.ofDeg(45,30)).y(),EPSILON);
        assertEquals(Angle.ofDeg(29.05537+180), stereographicProjection45.inverseApply(CartesianCoordinates.of(10,0)).az(),EPSILON);
        assertEquals(15,stereographicProjection.apply(stereographicProjection.inverseApply(CartesianCoordinates.of(10, 15))).y(), EPSILON);
        assertTrue(RightOpenInterval.of(0,Angle.TAU).contains(Angle.normalizePositive(-1.1102230246251565E-16)));
        assertEquals(0,stereographicProjection.inverseApply(stereographicProjection.apply(center)).alt(),  EPSILON);
        assertEquals(0,stereographicProjection.inverseApply(stereographicProjection.apply(center)).az(),  EPSILON);
        assertEquals(0,stereographicProjection45.apply(stereographicProjection45.inverseApply( CartesianCoordinates.of(5,0))).y(),EPSILON2);
    }

    @Test
    void circleCenterForParallel() {
        assertEquals(1/0. ,stereographicProjection0.circleCenterForParallel(center).y(), EPSILON);
        assertEquals(0.6089987401,stereographicProjection45.circleCenterForParallel(HorizontalCoordinates.ofDeg(
                0,27
        )).y(),EPSILON);
        assertEquals(0,stereographicProjection45.circleCenterForParallel(HorizontalCoordinates.ofDeg(
                0,27
        )).x());
    }

    @Test
    void circleRadiusForParallel() {
        assertEquals(1/0. ,stereographicProjection0.circleRadiusForParallel(center), EPSILON);
        assertEquals(0.7673831804,stereographicProjection45.circleRadiusForParallel(HorizontalCoordinates.ofDeg(
                0,27
        )),EPSILON);
    }

    @Test
    void applyToAngle() {
        assertEquals(4.363330053e-3,stereographicProjection.applyToAngle(Angle.ofDeg(1/2.0)),EPSILON);
    }

    @Test
    void testToString() {

        assertTrue(stereographicProjection.toString().contains("StereographicProjection"));
        assertTrue(stereographicProjection.toString().contains((String.format(Locale.ROOT,"%.4f",(Angle.ofDeg(23))))));
        assertTrue(stereographicProjection.toString().contains((String.format(Locale.ROOT,"%.4f",(Angle.ofDeg(45))))));

    }
}