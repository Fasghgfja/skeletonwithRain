package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Service
@Scope("application")
public class UserService {

    @Autowired
    private UserxRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    /**
     * Returns a collection of all users.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#setCreateDate} for new
     * entities or {@link Userx#setUpdateDate} for updated entities.
     * If the user did not exist before The Date of the operation will be stored with {@link Userx#setCreateDate}
     * and the user requesting the operation will be stored as the creator with  {@link Userx#setCreateUser(Userx)}
     * <p>
     * If the user already existed then update Date and the user doing the update
     * will be saved with  {@link Userx#setUpdateDate} {@link Userx#setUpdateUser(Userx)}
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(LocalDate.now());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(LocalDate.now());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * This method Deletes the user.
     * the method will delete the selected user and additionally
     * create an entry in the log stating who deleted the user and when.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        Log deleteLog = new Log();

        deleteLog.setDate(LocalDate.now());
        deleteLog.setAuthor(getAuthenticatedUser().getUsername());
        deleteLog.setSubject("USER DELETION");
        deleteLog.setText("DELETED USER: " + user.getUsername());

        logRepository.save(deleteLog);
        userRepository.delete(user);
    }

    /**
     * This Method returns the actual instance of the user.
     * @return the authenticated user.
     */
    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

}
