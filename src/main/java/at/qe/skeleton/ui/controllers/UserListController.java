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
 */
@Getter
@Setter
@Component
@Scope("view")
public class UserListController implements Serializable {

    private List<Userx> userxList;

    @Autowired
    private transient UserService userService;

    /**
     * Initializes a list of all users.
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

    public Collection<String> getGardenerNames() {
        return userService.getAllGardenerNames();
    }

    public Integer getUsersAmount() {
        return userService.getUsersAmount();
    }

}
