package at.qe.skeleton.model;



import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public class AccessPoint extends Metadata implements Persistable<Long>, Serializable, Comparable <AccessPoint> {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long accessPointId;

    @Column(length = 100)
    private String location;

    private boolean activated;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Long getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(Long accessPointId) {
        this.accessPointId = accessPointId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int compareTo(AccessPoint o) {
        return this.accessPointId.compareTo(o.getAccessPointId());
    }

    @Override
    public Long getId() {
        return this.accessPointId;
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }
}
