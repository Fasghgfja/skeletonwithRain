package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "USER_PLANT")
public class UserPlant implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Userx user;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANT_ID", nullable = false)
    private Plant plant;

    // constructors, getters, and setters
}