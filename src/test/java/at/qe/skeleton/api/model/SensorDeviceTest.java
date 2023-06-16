package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorDeviceTest {

    @Test
    void toString_ValidSensorDevice_ReturnsStringRepresentation() {
        SensorDevice sensorDevice = new SensorDevice();
        sensorDevice.setMac("Sample MAC");
        sensorDevice.setName("Sample name");
        sensorDevice.setUuid("Sample UUID");

        String expectedString = "SensorApi{" +
                ", uuid='Sample UUID" + '\'' +
                ", station_name='Sample name" + '\'' +
                ", mac=Sample MAC" +
                '}';

        String result = sensorDevice.toString();

        assertEquals(expectedString, result);
    }
}
