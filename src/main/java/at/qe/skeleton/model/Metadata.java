package at.qe.skeleton.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
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
    @ManyToOne(cascade = CascadeType.ALL)
    private Userx createUser;
    @ManyToOne
    private Userx updateUser;
}

