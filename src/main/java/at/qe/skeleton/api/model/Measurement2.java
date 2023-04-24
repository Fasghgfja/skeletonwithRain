package at.qe.skeleton.api.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class Measurement2 implements Serializable {


    private String sensorStation;
    private String sensor_id;
    private String value;
    private String time_stamp;


    @java.lang.Override
    public java.lang.String toString() {
        return "Measurement2{" +
                "sensorStation='" + sensorStation + '\'' +
                ", sensor_id='" + sensor_id + '\'' +
                ", value='" + value + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                '}';
    }
}