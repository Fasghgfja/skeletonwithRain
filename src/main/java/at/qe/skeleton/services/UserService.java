package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx createUser(String username, String password, String firstName, String lastName, String email, String phone, Set<UserRole> roles) {
        Userx userToBeCreated = new Userx(username, password, firstName, lastName, email, phone, roles);
        saveUser(userToBeCreated);
        Log createLog = new Log();

        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(getAuthenticatedUser().getUsername());
        createLog.setSubject("USER CREATION");
        createLog.setText("CREATED USER: " + userToBeCreated.getUsername());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
        return userToBeCreated;
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
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(getAuthenticatedUser().getUsername());
        deleteLog.setSubject("USER DELETION");
        deleteLog.setText("DELETED USER: " + user.getUsername());
        deleteLog.setType(LogType.SUCCESS);

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

    /**
     * Edits the user email.
     *
     * @param user the user to edit
     * @param email the new email for the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserEmail(Userx user, String email){
        user.setEmail(email);
        userRepository.save(user);
    }

    /**
     * Edits the users phone number.
     *
     * @param user the user to edit
     * @param phone the new email for the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserPhone(Userx user, String phone){
        user.setPhone(phone);
        userRepository.save(user);
    }

    /**
     * Edits the user firstName.
     *
     * @param user the user to edit
     * @param firstName the new firstName for the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserFirstName(Userx user, String firstName){
        user.setFirstName(firstName);
        userRepository.save(user);
    }

    /**
     * Edits the user lastName.
     *
     * @param user the user to edit
     * @param lastName the new lastName for the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserLastName(Userx user, String lastName){
        user.setLastName(lastName);
        userRepository.save(user);
    }

    /**
     * Edits the user password.
     *
     * @param user the user to edit
     * @param password the new password for the user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserPassword(Userx user, String password){
        user.setPassword(password);
        userRepository.save(user);
    }

    /**
     *
     * @param user the user to edit
     * @param roles the new roles assigned to this user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUserRoles(Userx user, Set<UserRole> roles){
        user.setRoles(roles);
        userRepository.save(user);
    }

}
