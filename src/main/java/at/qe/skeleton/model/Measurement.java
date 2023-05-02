package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Measurement implements Serializable {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "sensorStationName", nullable = true)
    private SensorStation sensorStation;


    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = true)
    private Plant plant;





    //TODO: JDBC is on crack and if this is called value its seen as a primary key and the table will not be created O_o, find a workaround
    private String value_s;
    private String unit;
    private String type;
    private LocalDate timestamp;

    @Override
    public String toString() {
        return "Measurement{" +
                "id: "+id +
                '}';
    }
}