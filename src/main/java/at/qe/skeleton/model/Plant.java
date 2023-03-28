package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


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
    @SequenceGenerator(name = "id_gen", initialValue = 1)
    @Column(nullable = false, unique = true)
    private Long plantID;

    private String description;

    @Column(length = 100)
    private String plantName;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.plantID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final Plant other)) {
            return false;
        }
        return Objects.equals(this.getPlantID(), other.getPlantID());
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.Text[ id=" + serialVersionUID + " , " + plantID + " ]";
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

}

