package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Entity representing Sensor stations.
 * <p>
 * Sensor stations have an Alarm-switch that can be On or Fixed,
 * The Alarm-switch is ON when any of the Alarm count of the Sensors of this Sensor Station
 * has exceeded the Sensor Station alarm count threshold(a light signal will be displayed on the sensor station).
 * The Alarm-switch is fixed when a gardener has solved the problem and manually sets it to fixed through the webapp
 * (light will stop blinking on the real sensor station).
 * When a admin or gardener sets the alarmswitch to fixed the respective alarmcount (-1) of the sensor causing it will be reset to 0 automatically
 * Sensor stations have an alarm count Threshold that is initialized with 5 by default
 * (this means that if any of its sensor alarm count reaches 5 then the alarm will be set to ON and on the sensor station a light will blink).
 * The alarm count threshold represents how high the alarm count of any given sensor of this sensor station can go before the system displays a warning light(at this point the alarmcount value of the sensor is changed to -1)
 *  and can be changed from manage sensor station page from an Admin (the real sensor station behaviour will be updated accordingly when this is done).

 The alarmcount of a given sensor can be resetted manually by a gardener or admin from the sensor station page aswell.
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

    @Column(length = 50)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    private Plant plant;


    @ManyToOne(fetch = FetchType.LAZY)
    AccessPoint accessPoint;

    @ManyToMany(mappedBy = "sensorStationsUnderCare")//TODO:LAZY OR NOT
    private Set<Userx> gardener = new HashSet<>();

    @Column
    private Integer alarmCountThreshold;

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
        return this.sensorStationName.compareTo(o.getSensorStationName());
    }

}
