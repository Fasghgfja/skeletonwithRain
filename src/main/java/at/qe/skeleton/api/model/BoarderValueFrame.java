package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoarderValueFrame {

    private Long sensor_id;


    private String lowerBoarder;
    private String upperBoarder;
    private String station_name;
    @Override
    public String toString() {
        return "SensorApi{" +
                "sensor_id=" + sensor_id +
                ", station_name='" + station_name + '\'' +
                '}';
    }
}
