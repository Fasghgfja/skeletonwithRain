package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SensorDevice implements Serializable {


    private String mac;
    private String name;
    private String uuid;

    @Override
    public String toString() {
        return "SensorApi{" +
                ", uuid='" + uuid + '\'' +
                ", station_name='" + name + '\'' +
                ", mac=" + mac +
                '}';
    }
}
