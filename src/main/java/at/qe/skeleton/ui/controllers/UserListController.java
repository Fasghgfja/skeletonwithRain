package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Getter
@Setter
@Component
@Scope("view")
public class UserListController implements Serializable {

    private List<Userx> userxList;

    @Autowired
    private UserService userService;

    /**
     * Returns a list of all users.
     *
     * @return
     */

    @PostConstruct
    public void init(){
        userxList = (ArrayList<Userx>) userService.getAllUsers();
    }

    public Collection<Userx> getUsers() {
        return userService.getAllUsers();
    }

    public Collection<Userx> getGardeners() {
        return userService.getAllGardeners();
    }
    /**
     * Frontend used to selkect a gardenre from a select many menu in sensor station menagement TODO: see if it can work with real gardeners instad
     */
    public Collection<String> getGardenerNames() {
        return userService.getAllGardenerNames();
    }



    public Integer getUsersAmount() {
        return userService.getUsersAmount();
    }

}
