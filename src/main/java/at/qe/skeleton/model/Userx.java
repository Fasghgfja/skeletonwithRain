package at.qe.skeleton.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */



@Entity
public class Userx extends Metadata implements Persistable<String>, Serializable, Comparable<Userx> {
    @Serial
    private static final long serialVersionUID = 1L;
    //TODO:ask is this really necessary?
    @Id
    @Column(length = 100)
    private String username;
    @ManyToOne(optional = false)
    private Userx createUser;
    @ManyToOne(optional = true)
    private Userx updateUser;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    boolean enabled;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

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