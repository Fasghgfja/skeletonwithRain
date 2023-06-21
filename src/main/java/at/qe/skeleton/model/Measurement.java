package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * This class represents the entity model for measurements.
 */
@Getter
@Setter
@Entity
public class Measurement  implements Serializable, Comparable<Measurement> {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorStationName", nullable = true)
    private SensorStation sensorStation;

    private String value_s;
    private String unit;
    private String type;
    private LocalDateTime timestamp;


    public String getReadableTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return timestamp.format(formatter);
    }

    public String getFormattedValue() {
        return String.format("%.1f",Double.parseDouble(this.value_s));

    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id: " + id +
                "Timestamp: " + getReadableTimestamp() +
                "unit: " + unit +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final Measurement other)) {
            return false;
        }
        return Objects.equals(this.id, other.getId());
    }

    @Override
    public int compareTo(Measurement measurement) {
        Double comp1 = Double.parseDouble(measurement.value_s);
        Double comp2 = Double.parseDouble(this.value_s);
        return comp1.compareTo(comp2);
    }
}