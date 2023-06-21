package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     The LogRepository is used to save logs for user interactions.
     */
    @Autowired
    private LogRepository logRepository;

    /**
     * Returns a collection of all users.
     */
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Method to get all gardeners currently saved in the database.
     * @return Collection of Userx with role 'GARDENER'
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GARDENER')")
    public Collection<Userx> getAllGardeners() {
        return userRepository.findByRole(UserRole.GARDENER);
    }

    /**
     * Method to get all names of the user with role 'GARDENER' currently saved in the database.
     */
    public Collection<String> getAllGardenerNames() {
        return userRepository.findNamesByRole(UserRole.GARDENER);
    }

    /**
     * Method to get the amount of users currently stored in the database.
     */

    public Integer getUsersAmount() {
        return userRepository.count();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GARDENER') or principal.username eq #username ")
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
     * Method to add a plant to the followed plants of the user
     * @param user Currently logged in user.
     * @param plant Plant that user likes to follow.
     */
    @PreAuthorize("permitAll()")
    public void addPlantToFollowedPlants(Userx user, Plant plant) {
        if(user == null || plant == null || plant.getFollowers().contains(user)) {return;}
        user = userRepository.findFirstByUsername(user.getUsername());
        user.getFollowedPlants().add(plant);
        userRepository.save(user);
    }

    /**
     * Removes an already followed plant from the users followed plants.
     * @param user Currently logged in user.
     * @param plant Plant to remove from the followed plants.
     */

    @PreAuthorize("permitAll()")
    public void removePlantFromFollowedPlants(Userx user, Plant plant) {
        if(user == null || plant == null) {return;}
        user = userRepository.findFirstByUsername(user.getUsername());
        user.getFollowedPlants().remove(plant);
        plant.getFollowers().remove(user);
        userRepository.save(user);
    }
}

