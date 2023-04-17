package at.qe.skeleton.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * JPA Entity to Represent an Image.
 */
@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    private Plant plant;

    @Column(length = 1000)
    private String imageName;

    @Column(columnDefinition = "DATE")
    private LocalDate creationDate;

    @Column(length = 50000000)
    private byte[] imageByte;

    private boolean approved;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }


    public LocalDate getDate() {
        return creationDate;
    }

    public void setDate(LocalDate date) {
        this.creationDate = date;
    }
}
