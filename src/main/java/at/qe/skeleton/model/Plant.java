package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


/**
 * Entity representing Plants.
 */
@Getter
@Setter
@Entity
public class Plant implements Persistable<Long>, Serializable, Comparable<Plant> {
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

    @ManyToMany(mappedBy = "followedPlants", fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Set<Userx> followers = new HashSet<>();


    @OneToOne(fetch = FetchType.LAZY )
    private SensorStation sensorStation;


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
        return (null == getPlantedDate());
    }

    @Override
    public int compareTo(Plant o) {
        return this.plantID.compareTo(o.getPlantID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plant plant)) return false;

        return getPlantID().equals(plant.getPlantID());
    }

    @Override
    public int hashCode() {
        return getPlantID().hashCode();
    }
}

