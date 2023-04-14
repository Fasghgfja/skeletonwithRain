package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class AccessPoint extends Metadata implements Persistable<Long>, Serializable, Comparable<AccessPoint>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public int compareTo(AccessPoint o) {
        return this.accessPointID.compareTo(o.getId());
    }
}
