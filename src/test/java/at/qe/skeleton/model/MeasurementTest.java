package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementTest {

    /**
     * Testing the getter and setter for the fields of the Measurement model.
     */

    @Test
    void testMeasurement(){
        Measurement measurement = new Measurement();

        Long id = 1L;
        String values = "1,2,3";
        SensorStation sensorStation = new SensorStation();
        String unit = "Celsius";
        String type = MeasurementType.HUMIDITY.getValue();
        LocalDateTime timestamp = LocalDateTime.now();

        assertNull(measurement.getId());
        assertNull(measurement.getSensorStation());
        assertNull(measurement.getValue_s());
        assertNull(measurement.getUnit());
        assertNull(measurement.getType());
        assertNull(measurement.getTimestamp());

        measurement.setId(id);
        measurement.setSensorStation(sensorStation);
        measurement.setValue_s(values);
        measurement.setUnit(unit);
        measurement.setType(type);
        measurement.setTimestamp(timestamp);

        assertEquals(id, measurement.getId());
        assertEquals(sensorStation, measurement.getSensorStation());
        assertEquals(values, measurement.getValue_s());
        assertEquals(unit, measurement.getUnit());
        assertEquals(type, measurement.getType());
        measurement.setType(MeasurementType.AIR_QUALITY.getValue());
        assertEquals(MeasurementType.AIR_QUALITY.toString(), measurement.getType());
        assertEquals(timestamp, measurement.getTimestamp());
        assertNotNull(measurement.getReadableTimestamp());
    }

    /**
     * Testing the hashCode() and equals() method of the Measurement model.
     */
    @Test
    void testEqualsAndHashCode(){
        Measurement measurement1 = new Measurement();
        Measurement measurement2 = new Measurement();
        measurement1.setId(1L);
        measurement2.setId(2L);
        Userx user = new Userx();

        assertEquals(measurement1.hashCode(), measurement1.hashCode());
        assertNotEquals(measurement1.hashCode(), measurement2.hashCode());
        assertNotEquals(measurement1, user);
    }

    /**
     * Testing the toString() method of the Measurement model.
     */
    @Test
    void testToString(){
        Measurement measurement = new Measurement();
        measurement.setId(1L);

        assertEquals("Measurement{id: 1}", measurement.toString());
    }
}
