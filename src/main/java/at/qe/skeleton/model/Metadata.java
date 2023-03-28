package at.qe.skeleton.model;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;


/**
 * Abstract Metadata to save and retrieve creatinDate and updateDate of entities.
 */
@MappedSuperclass
public abstract class Metadata {

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate createDate;
    @Column(columnDefinition = "DATE")
    private LocalDate updateDate;
    @ManyToOne
    private Userx createUser;
    @ManyToOne
    private Userx updateUser;

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}

