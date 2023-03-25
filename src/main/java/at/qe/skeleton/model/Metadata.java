package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDate;


/**
 * Abstract Metadata to save and retrieve createDate and updateDate of entities.
 */
@MappedSuperclass
public abstract class Metadata {

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate createDate;
    @Column(columnDefinition = "DATE")
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name="crateUser")
    private Userx createUser;

    @ManyToOne
    @JoinColumn(name="updateUser")
    private Userx updateUser;

    public Userx getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    public Userx getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

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

