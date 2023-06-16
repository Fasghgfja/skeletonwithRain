package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorStationApiTest {

    @Test
    void toString_ValidSensorStationApi_ReturnsStringRepresentation() {
        SensorStationApi sensorStationApi = new SensorStationApi();
        sensorStationApi.setName("Sample sensor station name");
        sensorStationApi.setService_description("Sample service description");
        sensorStationApi.setAlarm_switch("Sample alarm switch");

        String expectedString = "Measurement{" +
                "sensorStationName='Sample sensor station name" + '\'' +
                ", uuid='Sample service description" + '\'' +
                ", value='Sample alarm switch" + '\'' +
                '}';

        String result = sensorStationApi.toString();

        assertEquals(expectedString, result);
    }
}
