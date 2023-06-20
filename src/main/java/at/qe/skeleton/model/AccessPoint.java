package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;


/**
 * This is the entity model for the accessPoint.
 * The entity inherit the fields for create user(who created it) , update user(who last updated it) create date and update date from metadata.
 * <p>
 * An Access Point can Be validated or not (not by default)
 * Access Point is Validated when...
 */
@Getter
@Setter
@Entity
public class AccessPoint extends Metadata implements Persistable<Long>, Serializable, Comparable<AccessPoint> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, unique = true)
    private Long accessPointID;
    private String location;
    private boolean validated;
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
        return accessPointID.toString();
    }


}
