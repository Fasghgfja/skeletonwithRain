package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


//TODO: Fix like the example in the demo
/**
 * Entity representing an audit log entry.
 */
 @Getter
 @Setter
@Entity
public class Log implements Persistable<Long>, Serializable, Comparable<Log> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String text;

    @Column
    private String subject;

    @Column
    private String author;

    @Column(columnDefinition = "DATE")
    private LocalDate date;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private LogType type;


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
        if (!(obj instanceof final Log other)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }


    @Override
    public int compareTo(Log o) {
        return id.compareTo(o.getId());
    }

    @Override
    public boolean isNew() {
        return null == getTime();
    }
}
