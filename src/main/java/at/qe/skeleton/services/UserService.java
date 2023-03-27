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
 The UserService class provides methods for managing Userx objects and performing CRUD operations
 on the underlying UserxRepository. The class is annotated with @Scope("application") to ensure
 that a single instance of the UserService is created for the entire application.
 */
@Service
@Scope("application")
public class UserService {

    @Autowired
    private UserxRepository userRepository;

    /**
     The LogRepository is used to save logs for user deletions.
     */
    @Autowired
    private LogRepository logRepository;

    /**
     * Returns a collection of all users.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public Integer getUsersAmount() {
        return userRepository.findAll().stream().toList().size();
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
     Saves a user to the database. If the user is new, his create date and user are set. If the
     user already exists, his update date and user are set instead.
     The user is then saved to the UserxRepository.
     @param user the Userx object to save
     @return the updated Userx object
     */
    @PreAuthorize("permitAll()")
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(LocalDate.now());
            user.setCreateUser(user);
        } else {
            user.setUpdateDate(LocalDate.now());
            user.setUpdateUser(user);
        }
        return userRepository.save(user);
    }

    /**
     Deletes a user from the database and logs the deletion.
     The user to be deleted is passed as a parameter to the method. A Log object is created to record the deletion and saved to the
     LogRepository. The Userx object is then deleted from the UserxRepository.
     @param user the Userx object to delete
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
