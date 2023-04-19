package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




/**
 * Repository for managing {@link Userx} entities.
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 * usage:
 *  The Derived Query are split into two parts separated by keywords:
 *  The first one is the introducer(e.g find.., read.., query.., ...)
 *  The second one defines the criteria (e.g ...ByName, ...).
 */
public interface UserxRepository extends AbstractRepository<Userx, String> {



    /**
     * The method Retrieves a User entity by its Username.
     *
     * @param username must not be {@literal null}.
     * @return The user with the given name.
     * @throws IllegalArgumentException If username is {@literal null}.
     */
    Userx findFirstByUsername(String username);

    /**
     * The method Retrieves a List of User entities by part of its Username.
     *
     * @param username string that has to be present inside the username, must not be {@literal null}.
     * @return A list of users containing the given string in their username.
     * @throws IllegalArgumentException If username is {@literal null}.
     */
    List<Userx> findByUsernameContaining(String username);

    /**
     * The method Retrieves a List of User entities searching by a string containing both firstname and lastname separated
     * by a space.
     *
     * @param wholeName String composed by The First and second name of the user we want to search for separated by space
     *                  ,must not be {@literal null}.
     * @return A list of users corresponding to the searched wholeName.
     * @throws IllegalArgumentException If wholeName is {@literal null}.
     */
    @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

    /**
     * The method Retrieves a List of User entities searching by role {@link UserRole}.
     *
     * @param role UserRole to be searched for ,must not be {@literal null}.
     * @return a list of all users with the selected role.
     * @throws IllegalArgumentException If role is {@literal null}.
     */
    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    List<Userx> findByRole(@Param("role") UserRole role);

}
