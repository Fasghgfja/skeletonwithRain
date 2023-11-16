package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.UserService;

import java.io.Serializable;

import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Controller for the user detail view.
 * <p>
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

    @Autowired
    private transient PasswordEncoder passwordEncoder;

    @Autowired
    private transient LogRepository logRepository;

    @Autowired
    private transient UserListController userListController;

    private List<String> selectedRoles;

    private List<String> selectedRolesEdit;
    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;
    private String newPassword;
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
     * @param user user to set
     */
    public void setUser(Userx user) {
        this.user = user;
        this.selectedRolesEdit = new ArrayList<>();
        Set<UserRole> newRoles = this.user.getRoles();
        for (UserRole userRole : newRoles) {
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
                }
            }
        }
        user.setRoles(roles);
        user = this.userService.saveUser(user);
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("USER EDIT");
        createLog.setText("EDITED USER: " + user.getUsername());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User has been edited."));
        userListController.setUserxList((ArrayList<Userx>) userService.getAllUsers());
    }

    public void doSaveOwnUser() {
        this.userService.saveUser(user);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User saved successfully!", null));
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("USER EDIT");
        createLog.setText("EDITED USER: " + user.getUsername());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }

    public void doChangePassword() {
        if( newPassword != null && !Objects.equals(passwordEncoder.encode(newPassword), passwordEncoder.encode(user.getPassword()))){
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        this.userService.saveUser(user);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User saved successfully!", null));
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("USER EDIT");
        createLog.setText("EDITED USER: " + user.getUsername());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }


    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        user.setCreateUser(null);
        user.setUpdateUser(null);
        this.userService.deleteUser(user);
        user = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User has been deleted."));
        userListController.setUserxList((ArrayList<Userx>) userService.getAllUsers());
    }

    public List<String> getSelectedRolesEdit() {
        return selectedRolesEdit;
    }


    public void setSelectedRolesEdit(List<String> selectedRolesEdit) {
        this.selectedRolesEdit = selectedRolesEdit;
    }


    /**
     * Method to initialize a user view for the logged in user.
     */
    public void init() {
        if (this.user != null) {
            return;
        }
        Userx thisUser = sessionInfoBean.getCurrentUser();
        this.user = userService.loadUser(thisUser.getId());
    }


}


