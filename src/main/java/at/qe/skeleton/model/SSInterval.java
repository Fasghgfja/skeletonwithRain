package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;


@Getter
@Setter
@Entity
public class SSInterval implements Persistable<Long>, Serializable, Comparable<SSInterval>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, unique = true)
    private Long intervalId;

    @OneToOne
    private AccessPoint accessPoint;
    @Column(length = 100)
    private String measurementInterval;
    @Column(length = 100)
    private String webAppInterval;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SSInterval interval)) return false;

        return getIntervalId().equals(interval.getIntervalId());
    }

    @Override
    public int hashCode() {
        return getIntervalId().hashCode();
    }

    @Override
    public String toString() {
       return "MI: "+getMeasurementInterval() + "SI: " + getWebAppInterval();
    }


    @Override
    public Long getId() {
        return intervalId;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public int compareTo(SSInterval o) {
        return 0;
    }
}