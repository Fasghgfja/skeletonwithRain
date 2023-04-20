package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.InputStream;
import java.io.Serializable;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
@Scope("request")
public class UserDetailController implements Serializable {

    @Autowired
    private transient UserService userService;

    @Autowired
    private transient GalleryController galleryController;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private transient PasswordEncoder passwordEncoder;

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
        roles.add(UserRole.USER);
        if (selectedRolesEdit != null) {
            for (String role : selectedRolesEdit) {
                if (role.equals("ADMIN")) {
                    roles.add(UserRole.ADMIN);
                    roles.add(UserRole.GARDENER);
                } else if (role.equals("GARDENER")) {
                    roles.add(UserRole.GARDENER);
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

    public InputStream getProfilePicture() {
        if (user == null){return Faces.getResourceAsStream("images/awesomeProfilePicture.png");}
        InputStream input = galleryController.getProfilePicAsStreamedContent(""+user.getProfilePic().getId());
        return (input != null) ? input : Faces.getResourceAsStream("images/awesomeProfilePicture.png");
    }


    /**
     * Method to initialize a user view for the logged in user.
     */
    public void init() {
        if (this.user != null) {return;}
            Userx thisUser = sessionInfoBean.getCurrentUser();
            this.user = userService.loadUser(thisUser.getId());
        }

}


