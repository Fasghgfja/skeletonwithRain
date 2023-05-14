package at.qe.skeleton.model;

import java.io.Serializable;
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
@Entity
public class Userx extends Metadata implements Persistable<String>, Serializable, Comparable<Userx> {

    @Id
    @Column(length = 100)
    private String username;
    @ManyToOne(optional = false)
    private Userx createUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private Userx updateUser;

    @OneToOne
    private Image profilePic;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_plant",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "plant_id", referencedColumnName = "plantID")
    )
    private Set<Plant> followedPlants = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL)//TODO check if this can be lazy
    @JoinTable(
            name = "gardener_sensorStation",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "sensorStationName", referencedColumnName = "sensorStationName")
    )
    private Set<SensorStation> sensorStationsUnderCare = new HashSet<>();


















    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

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
        return "" + username;
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