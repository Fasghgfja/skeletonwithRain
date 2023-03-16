package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * Entity representing Plants.
 */
@Entity
@Table(name = "PLANT")
public class Plant extends Metadata implements Persistable<Long>, Serializable, Comparable<Plant> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", initialValue = 52)
    @Column(nullable = false, unique = true)
    private Long plantID;

    @Column(length = 100)
    private String plantName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Long getPlantID() {
        return plantID;
    }

    public void setPlantID(Long plantID) {
        this.plantID = plantID;
    }

    //TODO:Fix this ugly solution to make the class implement getId for persistable
    @Override
    public Long getId() {
        return getPlantID();
    }

    public void setId(Long id) {
        setPlantID(id);
    }



    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

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
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

    @Override
    public int compareTo(Plant o) {
        return this.plantID.compareTo(o.getPlantID());
    }

}

