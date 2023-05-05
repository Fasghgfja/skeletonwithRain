package at.qe.skeleton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/**
 * Abstract Metadata to save and retrieve creatinDate and updateDate of entities.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Metadata {

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate createDate;
    @Column(columnDefinition = "DATE")
    private LocalDate updateDate;
    @ManyToOne
    private Userx createUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private Userx updateUser;
}

