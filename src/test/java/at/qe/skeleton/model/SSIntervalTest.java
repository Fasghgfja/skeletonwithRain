package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SSIntervalTest {

    @Test
    void testSSInterval(){
        SSInterval interval = new SSInterval();
        SSInterval interval2 = new SSInterval();
        SensorStation sensorStation = new SensorStation();

        assertNull(interval.getIntervalId());
        assertNull(interval.getMeasurementInterval());
        assertNull(interval.getSensorStation());
        assertNull(interval.getWebAppInterval());

        interval.setIntervalId(1L);
        interval2.setIntervalId(2L);
        interval.setSensorStation(sensorStation);
        interval.setMeasurementInterval("5");
        interval.setWebAppInterval("2");

        assertEquals(1L, interval.getId());
        assertEquals(sensorStation, interval.getSensorStation());
        assertEquals("5", interval.getMeasurementInterval());
        assertEquals("2", interval.getWebAppInterval());
        assertNotEquals(interval, interval2);
        assertNotEquals(interval.hashCode(), interval2.hashCode());

        assertEquals("MI: 5SI: 2", interval.toString());
        assertTrue(interval.compareTo(interval2) < 0);
        assertTrue(interval2.compareTo(interval) > 0);
    }
}
