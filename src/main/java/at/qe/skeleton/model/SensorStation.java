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
    //added to view alarm

    @Column(length = 100)
    private String alarmSwitch;

    //added to view SensorStation description
    @Column(length = 50)
    private String description;

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

        return getSensorStationName().equals(that.getSensorStationName());
    }

    @Override
    public int hashCode() {
        return getSensorStationName().hashCode();
    }

    @Override
    public String toString() {
        return sensorStationName;
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
    // needed to have a view on alarm

    public String getAlarmSwitch() {
        return alarmSwitch;
    }

    public void setAlarmSwitch(String alarmSwitch) {
        this.alarmSwitch = alarmSwitch;
    }
    // needed for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void alarmSwitchOn() {
        this.alarmSwitch = "on";
    }

    public void alarmSwitchOff() {
        this.alarmSwitch = "off";
    }


}
