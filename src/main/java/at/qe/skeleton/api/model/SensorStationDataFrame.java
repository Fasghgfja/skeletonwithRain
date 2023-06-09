package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SensorStationDataFrame implements Serializable {

    private Long sensor_id;
    private String sensorType;
    private String lowerBoarder;
    private String upperBoarder;
    private String station_name;
    private int measurementInterval;
    private int webappSendInterval;
    private int alarmCountThreshold;

    @Override
    public String toString() {
        return "SensorStationDataFrame{" +
                "sensor_id=" + sensor_id +
                ", sensorType='" + sensorType + '\'' +
                ", lowerBoarder='" + lowerBoarder + '\'' +
                ", upperBoarder='" + upperBoarder + '\'' +
                ", station_name='" + station_name + '\'' +
                ", measurementInterval=" + measurementInterval +
                ", webappSendInterval=" + webappSendInterval +
                ", alarmCountThreshold=" + alarmCountThreshold +
                '}';
    }
}
