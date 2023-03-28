package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing Sensor stations.
 */
@Getter
@Setter
@Entity
public class SensorStation extends Metadata implements Persistable<Long>, Serializable, Comparable<SensorStation> {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long sensorStationID;

    @Column(length = 100)
    private String location;


    @OneToOne
    @JoinColumn( nullable = true)
    private Plant plant;

    public String getSensorStationLocation() {
        return location;
    }

    public void setSensorStationLocation(String location) {
        this.location = location;
    }

    public Long getSensorStationID() {
        return sensorStationID;
    }
    public Long getId() {
        return this.getSensorStationID();
    }

    public void setSensorStationID(Long sensorStationID) {
        this.sensorStationID = sensorStationID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sensorStationID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final Plant other)) {
            return false;
        }
        return Objects.equals(this.getSensorStationID(), other.getPlantID());
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.Text[ id=" + sensorStationID + " , " + sensorStationID + " ]";
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

    @Override
    public int compareTo(SensorStation o) {
        return this.sensorStationID.compareTo(o.getSensorStationID());
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}
