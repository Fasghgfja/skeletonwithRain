package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;


import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

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

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private List<String> selectedRoles;

    private List<String> selectedRolesEdit;
    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;

    /**
     * Returns the currently displayed user.
     */
    public Userx getUser() {
        return user;
    }

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
        this.selectedRolesEdit = new ArrayList<>();
        Set<UserRole> newRoles  = this.user.getRoles();
        for (UserRole userRole: newRoles) {
            this.selectedRolesEdit.add(userRole.name());
        }

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
        Set<UserRole> roles = new HashSet<>();
        if (selectedRolesEdit != null) {
            for (String role : selectedRolesEdit) {
                if (role.equals("ADMIN")) {
                    roles.add(UserRole.ADMIN);
                    roles.add(UserRole.GARDENER);
                    roles.add(UserRole.USER);
                } else if (role.equals("GARDENER")) {
                    roles.add(UserRole.GARDENER);
                    roles.add(UserRole.USER);
                } else{
                    roles.add(UserRole.USER);
                }
            }
        }
        user.setRoles(roles);
        user = this.userService.saveUser(user);
    }


    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }

    public List<String> getSelectedRolesEdit() {
        return selectedRolesEdit;
    }

    public void doAddPlantToFollowedPlants(Plant plant) {
        user = sessionInfoBean.getCurrentUser();
        this.userService.addPlantToFollowedPlants(user,plant);
    }

    public void setSelectedRolesEdit(List<String> selectedRolesEdit) {
        this.selectedRolesEdit = selectedRolesEdit;
    }

}
