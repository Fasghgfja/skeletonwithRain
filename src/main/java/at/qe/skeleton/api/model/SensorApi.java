package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorApi {

    private Long sensor_id;
    private String uuid;
    private String station_name;
    private String type;
    private int alarm_count;

    private String upperBoarder;

    private String lowerBoarder;

    @Override
    public String toString() {
        return "SensorApi{" +
                "sensor_id=" + sensor_id +
                ", uuid='" + uuid + '\'' +
                ", station_name='" + station_name + '\'' +
                ", type='" + type + '\'' +
                ", alarm_count=" + alarm_count +
                '}';
    }
}
