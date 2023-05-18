package at.qe.skeleton.api.model;


import at.qe.skeleton.model.Plant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorStationApi {

    private String name;
    private String service_description;
    private String alarm_switch;
    @Override
    public String toString() {
        return "Measurement{" +
                "sensorStationName='" + name + '\'' +
                ", uuid='" + service_description + '\'' +
                ", value='" + alarm_switch + '\'' +
                '}';
    }


}
