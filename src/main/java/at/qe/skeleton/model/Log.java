package at.qe.skeleton.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


//TODO: Fix like the example in the demo
/**
 * Entity representing an audit log entry.
 */
 @Getter
 @Setter
@Entity
public class Log {

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
}
