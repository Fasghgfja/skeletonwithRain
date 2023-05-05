package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
public class AccessPoint extends Metadata implements Persistable<Long>, Serializable, Comparable<AccessPoint>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, unique = true)
    private Long accessPointID;
    private String location;
    private boolean validated;//TODO: this is gonna be important


    @Override
    public Long getId() {
        return this.accessPointID;
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.accessPointID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final AccessPoint other)) {
            return false;
        }
        return Objects.equals(this.accessPointID, other.accessPointID);
    }

    @Override
    public int compareTo(AccessPoint o) {
        return this.accessPointID.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return ""+accessPointID;
    }


}
