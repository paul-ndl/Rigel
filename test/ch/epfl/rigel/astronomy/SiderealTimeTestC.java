package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class SiderealTimeTestC {

    @Test
    void greenwichWorksWithKnownValue() {
        assertEquals(4.894899243459,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0),
                        ZoneId.of("UTC"))), 1e-4);

        assertEquals(5.274208582903,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(1995, Month.JANUARY, 9),
                        LocalTime.of(12, 54, 23),
                        ZoneId.of("UTC"))), 1e-4);

        assertEquals(2.209444098114,
                SiderealTime.greenwich(ZonedDateTime.of(LocalDate.of(2018, Month.SEPTEMBER, 9),
                        LocalTime.of(9, 12, 52),
                        ZoneId.of("UTC"))), 1e-6);
    }

}