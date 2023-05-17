package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * JPA Entity to Represent an Image , this is used for profile pictures and plant images.
 */
@Getter
@Setter
@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", initialValue = 100)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plant plant;

    @Column(length = 1000)
    private String imageName;

    @Column(columnDefinition = "DATE")
    private LocalDate creationDate;

    @Column(length = 50000000)
    private byte[] imageByte;


    private boolean approved;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public LocalDate getDate() {
        return creationDate;
    }

    public void setDate(LocalDate date) {
        this.creationDate = date;
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
        if (!(obj instanceof final Image other)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
}
