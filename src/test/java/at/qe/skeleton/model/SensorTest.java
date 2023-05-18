package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorTest {

    @Test
    void testSensor(){
        SensorStation sensorStation = new SensorStation();
        Sensor sensor = new Sensor();
        Sensor sensor2 = new Sensor();
        Sensor sensor3 = new Sensor();
        sensor.setId(1L);
        sensor2.setId(2L);
        sensor3.setId(1L);
        sensor.setUuid("UUID");
        sensor.setSensorStation(sensorStation);
        sensor.setType(MeasurementType.AIR_QUALITY.getValue());
        sensor.setAlarm_count(5);
        sensor.setUpper_border("10");
        sensor.setLower_border("1");
        String toStringExpected = "Sensor{" +
                "type='" + sensor.getType() + '\'' +
                ", upper_border='" + sensor.getUpper_border() + '\'' +
                ", lower_border='" + sensor.getLower_border() + '\'' +
                '}';

        assertEquals(1L, sensor.getId());
        assertEquals("UUID", sensor.getUuid());
        assertEquals(sensorStation, sensor.getSensorStation());
        assertEquals(MeasurementType.AIR_QUALITY.toString(), sensor.getType());
        assertEquals(5, sensor.getAlarm_count());
        assertEquals("10", sensor.getUpper_border());
        assertEquals("1", sensor.getLower_border());
        assertEquals(toStringExpected, sensor.toString());
        assertNotEquals(sensor.hashCode(), sensor2.hashCode());
        assertEquals(sensor.hashCode(), sensor3.hashCode());
    }
}
