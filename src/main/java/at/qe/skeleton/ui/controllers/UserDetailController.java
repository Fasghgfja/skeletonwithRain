package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import at.qe.skeleton.model.UserRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */

@Getter
@Setter
@Component
@Scope("view")
public class UserDetailController implements Serializable {

    @Autowired
    private UserService userService;

    /**
     * Attribute to cache the currently displayed user
     */
    @Setter(AccessLevel.NONE)
    private Userx user;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Set<UserRole> roles;
    private boolean admin;
    private boolean gardener;

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(Userx user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
        user = this.userService.saveUser(user);
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }

    public void doCreateUser() {
        roles = new HashSet<>();
        roles.add(UserRole.USER);
        if(admin){
            roles.add(UserRole.GARDENER);
            roles.add(UserRole.ADMIN);
        } else if(gardener){
            roles.add(UserRole.GARDENER);
        }
        this.userService.createUser(username,password, firstName,lastName,email, phone, roles);
    }

    public void doAddPlantToFollowedPlants(Userx user, Plant plant) {
        this.userService.addPlantToFollowedPlants(user,plant);
    }


}
