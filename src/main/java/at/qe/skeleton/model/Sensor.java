package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.io.Serializable;


/**
 * Entity representing The Sensors of the Sensor Stations.
 * The entity inherit the fields for create user(who created it) , update user(who last updated it) create date and update date from metadata.
 * A sensor is added to the system by its respective sensor station.
 * Each Sensor Station has actual sensors that take the measurements.
 * Sensors have a type:
 * SOIL_MOISTURE,
 * HUMIDITY,
 * AIR_PRESSURE,
 * TEMPERATURE,
 * AIR_QUALITY,
 * LIGHT_INTENSITY
 * Sensors have a Upper border and a Lower border representing their respective thresholds and
 * Sensors have a alarm count that goes up every time they take a measurement that exceeds the threshold
 * When a alarmcount exceeds its threshold then its value will be set to -1 and a light will start blinking on the sensor station (alarmswitch of the sensor station will be ON).
 * When a gardener sets the alarmswitch to fixed then the -1 of the respective sensor alarmcount will be set to 0...
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
