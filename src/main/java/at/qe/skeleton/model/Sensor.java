package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing Sensor stations.
 */
@Getter
@Setter
@Entity
public class Sensor  extends Metadata implements Persistable<Long>, Serializable {
    @Id
    private Long id;
    @Column(length = 100)
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "sensorStationName", nullable = true)
    private SensorStation sensorStation;

    @Column(length = 100)
    private String type;

    private int alarm_count;

    private String upper_border;

    private String lower_border;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return getId().equals(sensor.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }


    @Override
    public String toString() {
        return "Sensor{" +
                "type='" + type + '\'' +
                ", upper_border='" + upper_border + '\'' +
                ", lower_border='" + lower_border + '\'' +
                '}';
    }
}
