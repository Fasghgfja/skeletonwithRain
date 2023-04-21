package at.qe.skeleton.api.model;

import at.qe.skeleton.model.SensorStation;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class Measurement implements Serializable {


    private Long id;
    private SensorStation sensorStation;

    private String value_s;
    private String unit;
    private String type;
    private LocalDate timestamp;


}