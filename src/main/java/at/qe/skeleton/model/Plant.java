package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Entity representing Plants.
 */
@Getter
@Setter
@Entity
public class Plant extends Metadata implements Persistable<Long>, Serializable, Comparable<Plant> {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", initialValue = 2)
    @Column(nullable = false, unique = true)
    private Long plantID;

    private String description;

    @Column(columnDefinition = "DATE")
    private LocalDate plantedDate;

    @Column(length = 100)
    private String plantName;

    @ManyToMany(mappedBy = "followedPlants", fetch = FetchType.LAZY)
    private Set<Userx> followers = new HashSet<>();

    @ManyToMany(mappedBy = "plantsUnderCare", fetch = FetchType.LAZY)
    private Set<Userx> gardeners = new HashSet<>();




    @Override
    public String toString() {
        return  plantName ;
    }


    @Override
    public Long getId() {
        return this.getPlantID();
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

    @Override
    public int compareTo(Plant o) {
        return this.plantID.compareTo(o.getPlantID());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plant plant = (Plant) o;

        if (!Objects.equals(plantID, plant.plantID)) return false;
        if (!Objects.equals(description, plant.description)) return false;
        return Objects.equals(plantName, plant.plantName);
    }

    @Override
    public int hashCode() {
        int result = plantID != null ? plantID.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (plantName != null ? plantName.hashCode() : 0);
        return result;
    }
}

