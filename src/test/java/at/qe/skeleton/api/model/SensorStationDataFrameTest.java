package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorStationDataFrameTest {

    @Test
    void toString_ValidSensorStationDataFrame_ReturnsStringRepresentation() {
        SensorStationDataFrame sensorStationDataFrame = new SensorStationDataFrame();
        sensorStationDataFrame.setSensor_id(1L);
        sensorStationDataFrame.setSensorType("Sample sensor type");
        sensorStationDataFrame.setLowerBoarder("Sample lower border");
        sensorStationDataFrame.setUpperBoarder("Sample upper border");
        sensorStationDataFrame.setStation_name("Sample station name");
        sensorStationDataFrame.setMeasurementInterval(60);
        sensorStationDataFrame.setWebappSendInterval(120);
        sensorStationDataFrame.setAlarmCountThreshold(5);

        String expectedString = "SensorStationDataFrame{" +
                "sensor_id=1" +
                ", sensorType='Sample sensor type" + '\'' +
                ", lowerBoarder='Sample lower border" + '\'' +
                ", upperBoarder='Sample upper border" + '\'' +
                ", station_name='Sample station name" + '\'' +
                ", measurementInterval=60" +
                ", webappSendInterval=120" +
                ", alarmCountThreshold=5" +
                '}';

        String result = sensorStationDataFrame.toString();

        assertEquals(expectedString, result);
    }
}
