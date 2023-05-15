package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Measurement implements Serializable {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "sensorStationName", nullable = true)
    private SensorStation sensorStation;

    private String value_s;
    private String unit;
    private String type;
    private LocalDateTime timestamp;


    public String getReadableTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id: "+id +
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
}