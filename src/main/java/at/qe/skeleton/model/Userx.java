package at.qe.skeleton.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Userx extends Metadata implements Persistable<String>, Serializable, Comparable<Userx> {

    @Id
    @Column(length = 100)
    private String username;
    @ManyToOne(optional = false)
    private Userx createUser;
    @ManyToOne
    private Userx updateUser;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_plant",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "plant_id", referencedColumnName = "plantID")
    )
    private Set<Plant> followedPlants = new HashSet<>();


    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    /**
     * Constructor to create user via pop-up in webapp
     * @param username unique username
     * @param password password of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param email email of the user
     * @param phone phone number of the user
     * @param roles Roles of the user
     */
    public Userx(String username, String password, String firstName, String lastName, String email, String phone, Set<UserRole> roles) {
        this.username = username;
        this.createUser = null;
        this.updateUser = null;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
        this.enabled = true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof final Userx other)) {
            return false;
        }
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == super.getCreateDate());
    }

	@Override
	public int compareTo(Userx o) {
		return this.username.compareTo(o.getUsername());
	}

}