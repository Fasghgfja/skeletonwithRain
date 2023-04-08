package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    private transient UserService userService;

    /**
     * Attributes to cache the currently displayed user
     */
    private Userx user;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Set<UserRole> roles = new HashSet<>();
    private boolean gardener;
    private boolean admin;

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
        getRoles().add(UserRole.USER);
        if(admin){
            getRoles().add(UserRole.GARDENER);
            getRoles().add(UserRole.ADMIN);
        } else if(gardener){
            getRoles().add(UserRole.GARDENER);
        }
        this.userService.createUser(username,password, firstName,lastName,email, phone, roles);
    }

    private Userx userToBeEdited;

    private String editUsername;
    private String editPassword;
    private String editFirstName;
    private String editLastName;
    private String editEmail;
    private String editPhone;
    private boolean editAdmin;

    private boolean editGardener;

    public void doEditUser(){
        this.userService.editUserFirstName(userToBeEdited, editFirstName);
        this.userService.editUserLastName(userToBeEdited, editLastName);
        this.userService.editUserEmail(userToBeEdited, editEmail);
        this.userService.editUserPhone(userToBeEdited, editPhone);
        userToBeEdited.getRoles().add(UserRole.USER);
        if (editAdmin) {
            userToBeEdited.getRoles().add(UserRole.GARDENER);
            userToBeEdited.getRoles().add(UserRole.ADMIN);
        } else if(editGardener){
            userToBeEdited.getRoles().add(UserRole.GARDENER);
        }
        this.userService.editUserRoles(userToBeEdited, userToBeEdited.getRoles());
    }


    public void showUserEditDialog(){
        this.editUsername = userToBeEdited.getUsername();
        this.editFirstName = userToBeEdited.getFirstName();
        this.editLastName = userToBeEdited.getLastName();
        this.editEmail = userToBeEdited.getEmail();
        this.editAdmin = userToBeEdited.getRoles().contains(UserRole.ADMIN);
        this.editGardener = userToBeEdited.getRoles().contains(UserRole.GARDENER);
    }


}
