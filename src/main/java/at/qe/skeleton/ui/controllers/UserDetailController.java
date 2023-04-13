package at.qe.skeleton.ui.controllers;

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
import org.springframework.stereotype.Controller;

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
@Controller
public class UserDetailController implements Serializable {

    @Autowired
    private UserService userService;

    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;
    private Userx userToBeCreated = new Userx();
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
        this.userToBeCreated = null;
    }


}
