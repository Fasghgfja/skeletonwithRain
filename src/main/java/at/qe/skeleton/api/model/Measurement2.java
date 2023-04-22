package at.qe.skeleton.api.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class Measurement2 implements Serializable {


    private String sensorStationName;
    private String uuid;
    private String value;


    @Override
    public String toString() {
        return "Measurement{" +
                "sensorStationName='" + sensorStationName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", value='" + value + '\'' +
                '}';
    }


}