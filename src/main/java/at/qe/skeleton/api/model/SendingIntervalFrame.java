package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SendingIntervalFrame implements Serializable {


    private int measurementInterval;
    private int webappSendInterval;
    private int alarmCountThreshold;
    @Override
    public String toString() {
        return "SensorApi{" +
                ", station_name='" + measurementInterval + '\'' +
                ", station_name='" + webappSendInterval + '\'' +
                '}';
    }
}
