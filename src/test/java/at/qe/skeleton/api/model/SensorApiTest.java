package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorApiTest {

    @Test
    void toString_ValidSensorApi_ReturnsStringRepresentation() {
        SensorApi sensorApi = new SensorApi();
        sensorApi.setSensor_id(1L);
        sensorApi.setUuid("Sample UUID");
        sensorApi.setStation_name("Sample station name");
        sensorApi.setType("Sample type");
        sensorApi.setAlarm_count(5);

        String expectedString = "SensorApi{" +
                "sensor_id=1" +
                ", uuid='Sample UUID" + '\'' +
                ", station_name='Sample station name" + '\'' +
                ", type='Sample type" + '\'' +
                ", alarm_count=5" +
                '}';

        String result = sensorApi.toString();

        assertEquals(expectedString, result);
    }
}
