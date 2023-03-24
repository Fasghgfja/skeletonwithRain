package at.qe.skeleton.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Measurement implements Serializable {

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long id;
    @Column
    private Long sensorStationID;
    @Column
    private Long plantID;
    @Column
    private Double value;
    @Column
    private String unit;
    @Column
    private MeasurementType type;
    @Column
    private LocalDate timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlantID() {
        return plantID;
    }


    public void setPlantID(Long plantID) {
        this.plantID = plantID;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MeasurementType getType() {
        return type;
    }

    public void setType(MeasurementType type) {
        this.type = type;
    }
}
