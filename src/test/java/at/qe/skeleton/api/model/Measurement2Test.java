package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Measurement2Test {

    @Test
    void toString_ValidMeasurement2_ReturnsStringRepresentation() {
        Measurement2 measurement2 = new Measurement2();
        measurement2.setSensorStation("Sample sensor station");
        measurement2.setSensor_id("Sample sensor ID");
        measurement2.setValue("Sample value");
        measurement2.setTime_stamp("2023-06-14 12:34:56");

        String expectedString = "Measurement2{" +
                "sensorStation='Sample sensor station" + '\'' +
                ", sensor_id='Sample sensor ID" + '\'' +
                ", value='Sample value" + '\'' +
                ", time_stamp='2023-06-14 12:34:56" + '\'' +
                '}';

        String result = measurement2.toString();

        assertEquals(expectedString, result);
    }
}
