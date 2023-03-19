package at.qe.skeleton.model;


import jakarta.persistence.*;
import java.time.LocalDate;



/**
 * JPA Entity to Represent an Image.
 */
@Entity
public class Image {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long imageId;


    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column
    private String imageName;

    @Column
    private String author;

    @Column(columnDefinition = "DATE")
    private LocalDate creationDate;



    @Column(length = 50000000)
    private byte[] imageByte;

    public Image() {

    }

    public Image(String imageName, byte[] picByte) {
        this.imageName = imageName;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Long getId() {return imageId;}

    public void setId(Long imageId) {this.imageId = imageId;}

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDate() {
        return creationDate;
    }

    public void setDate(LocalDate date) {
        this.creationDate = date;
    }
}
