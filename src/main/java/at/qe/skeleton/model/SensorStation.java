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
public class SensorStation extends Metadata implements Persistable<String>, Serializable, Comparable<SensorStation> {
    @Id
    @Column(length = 100)
    private String sensorStationName;

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

    public String getSensorStationID() {
        return sensorStationName;
    }
    public String getId() {
        return this.getSensorStationID();
    }

    public void setSensorStationID(String sensorStationID) {
        this.sensorStationName = sensorStationID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorStation that = (SensorStation) o;

        if (getSensorStationName() != null ? !getSensorStationName().equals(that.getSensorStationName()) : that.getSensorStationName() != null)
            return false;
        return getPlant() != null ? getPlant().equals(that.getPlant()) : that.getPlant() == null;
    }

    @Override
    public int hashCode() {
        int result = getSensorStationName() != null ? getSensorStationName().hashCode() : 0;
        result = 31 * result + (getPlant() != null ? getPlant().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.Text[ id=" + sensorStationName + " , " + sensorStationName + " ]";
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

    @Override
    public int compareTo(SensorStation o) {
        return this.sensorStationName.compareTo(o.getSensorStationID());
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}
