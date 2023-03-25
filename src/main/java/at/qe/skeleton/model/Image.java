package at.qe.skeleton.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * JPA Entity to Represent an Image.
 */
@Entity
public class Image implements Serializable {

    String imageSrc;

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = true)
    private Plant plant;


    @Column
    private String imageName;

    @Column
    @JoinColumn(name="uploader")
    private Userx uploader;

    @Column(columnDefinition = "DATE")
    private LocalDate creationDate;


    @Column(length = 50000000)
    private byte[] imageByte;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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
