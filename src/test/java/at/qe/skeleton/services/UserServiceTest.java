package at.qe.skeleton.services;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.repositories.UserxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserRole;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the userService class.
 */
@SpringBootTest
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserxRepository userRepository;

    @Autowired
    PlantService plantService;

    /**
     * Test saveUser() method of userService.
     * This method can be called by any role. Therefore, will be tested without any authorities.
     */
    @DirtiesContext
    @Test
    void testSaveUser(){
        int initialUserCount = userRepository.count();
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("testUserToBeSaved");
        userService.saveUser(userToBeSaved);

        assertEquals(initialUserCount + 1, userRepository.count());
        assertEquals(userToBeSaved, userRepository.findFirstByUsername("testUserToBeSaved"));
        assertNotNull(userToBeSaved.getCreateUser());
        assertNotNull(userToBeSaved.getCreateDate());
    }

    /**
     * Testing the deleteUser() method of the userService.
     * This can only be done by an user with role 'ADMIN'.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteUserWithAdmin(){
        int initialUserCount = userRepository.count();
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("testUserToBeSaved");
        userService.saveUser(userToBeSaved);

        assertEquals(initialUserCount + 1, userRepository.count());
        assertEquals(userToBeSaved, userRepository.findFirstByUsername("testUserToBeSaved"));
        assertNotNull(userToBeSaved.getCreateUser());
        assertNotNull(userToBeSaved.getCreateDate());

        userService.deleteUser(userToBeSaved);

        assertEquals(initialUserCount, userRepository.count());
        assertNull(userRepository.findFirstByUsername("testUserToBeSaved"));
    }

    /**
     * Testing the deleteUser() method of the userService with a non admin user.
     * This should not work since the user does not have role 'ADMIN'.
     */

    @Test
    @WithMockUser(username = "admin", authorities = {"GARDENER"})
    void testDeleteUserWithNonAdmin(){
        int initialUserCount = userRepository.count();
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("testUserToBeSaved");
        userService.saveUser(userToBeSaved);

        assertEquals(initialUserCount + 1, userRepository.count());
        assertEquals(userToBeSaved, userRepository.findFirstByUsername("testUserToBeSaved"));
        assertNotNull(userToBeSaved.getCreateUser());
        assertNotNull(userToBeSaved.getCreateDate());

        assertThrows(AccessDeniedException.class, () -> {
            userService.deleteUser(userToBeSaved);
        });
    }

    /**
     * Testing getAllUsers() method of the userService.
     * This should only work for users with role 'ADMIN'.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllUsersAdmin(){
        List<Userx> initialListOfUsers = (ArrayList<Userx>) userService.getAllUsers();

        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        Userx user3 = new Userx();
        user3.setUsername("user3");

        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        initialListOfUsers.add(user1);
        initialListOfUsers.add(user2);
        initialListOfUsers.add(user3);

        assertEquals(initialListOfUsers, userService.getAllUsers());
    }

    /**
     * Testing getAllUsers() method of the userService.
     * This should only work for users with role 'ADMIN'.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void testGetAllUsersNonAdmin(){
        assertThrows(AccessDeniedException.class, userService::getAllUsers);
    }

    /**
     * Testing getAllGardeners() method of the userService.
     * This should only work for users with role 'ADMIN'.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllGardenersAdmin(){
        List<Userx> initialListOfGardeners = (ArrayList<Userx>) userService.getAllGardeners();

        // We are not setting the role to 'GARDENER' for user1 in order to check that only gardeners are returned.
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        user2.setRoles(Set.of(UserRole.GARDENER));
        Userx user3 = new Userx();
        user3.setUsername("user3");
        user3.setRoles(Set.of(UserRole.GARDENER));


        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        initialListOfGardeners.add(user2);
        initialListOfGardeners.add(user3);

        assertEquals(initialListOfGardeners, userService.getAllGardeners());
    }


    /**
     * Testing getAllGardeners() method of the userService.
     * This should only work for users with role 'ADMIN'.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"USER"})
    void testGetAllGardenersNonAdmin(){

        assertThrows(AccessDeniedException.class, userService::getAllGardeners);
    }

    /**
     * Testing getAllGardenerNames() method of the userService.
     */

    @DirtiesContext
    @Test
    void testGetAllGardenerNames(){
        List<String> initialListOfGardenerNames = (ArrayList<String>) userService.getAllGardenerNames();

        // We are not setting the role to 'GARDENER' for user1 in order to check that only gardeners are returned.
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        user2.setRoles(Set.of(UserRole.GARDENER));
        Userx user3 = new Userx();
        user3.setUsername("user3");
        user3.setRoles(Set.of(UserRole.GARDENER));


        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        initialListOfGardenerNames.add("user2");
        initialListOfGardenerNames.add("user3");

        System.out.println(initialListOfGardenerNames);
        assertEquals(initialListOfGardenerNames, userService.getAllGardenerNames());
    }

    /**
     * Testing the getUsersAmount() method of the userService.
     */

    @DirtiesContext
    @Test
    void testGetUsersAmount(){
        int initialUsersAmount = userService.getUsersAmount();

        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        Userx user3 = new Userx();
        user3.setUsername("user3");

        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        assertEquals(initialUsersAmount + 3, userService.getUsersAmount());
    }

    /**
     * Users cannot be saved with empty username since username is identifier in the datatable.
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testExceptionForEmptyUsername() {
        Userx toBeCreatedUser = new Userx();
        assertThrows(JpaSystemException.class, () -> {
            userService.saveUser(toBeCreatedUser);
        });
    }

    /**
     * Testing the deleteUser() method of the userService.
     * Only users with role 'ADMIN' are able to delete users.
     */

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void testUnauthorizedDeleteUser() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        userService.saveUser(user1);
        assertThrows(AccessDeniedException.class, () -> userService.deleteUser(user1));

    }


    /**
     * Testing the loadUser method of the userService.
     * This should only work if the user has role 'ADMIN' or tries to load himself/herself.
     * User with username "user1" and role 'USER' loads user with username "admin".
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void testUnauthorizedLoadUser() {
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("user1");
        userService.saveUser(userToBeSaved);

        assertThrows(AccessDeniedException.class, () -> userService.loadUser("admin"));
    }

    /**
     * Testing the loadUser method of the userService.
     * This should only work if the user has role 'ADMIN' or tries to load himself/herself.
     * User with username "admin" and role 'ADMIN' loads user with username "user1".
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAuthorizedLoadUser(){
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("user1");
        userService.saveUser(userToBeSaved);

        assertEquals(userToBeSaved, userService.loadUser("user1"));
    }

    /**
     * Testing the loadUser method of the userService.
     * This should only work if the user has role 'ADMIN' or tries to load himself/herself.
     * User with username "user1" and role 'USER' loads user with username "user1".
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"User"})
    void testAuthorizedLoadUserNonAdmin(){
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("user1");
        userService.saveUser(userToBeSaved);

        assertEquals(userToBeSaved, userService.loadUser("user1"));
    }

    /**
     * Testing the addPlantToFollowedPlants(Userx user, Plant plant) and
     * removePlantFromFollowedPlants(Userx user, Plant plant) methods of the userService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", authorities = {"User"})
    void testAddAndRemovePlantFromFollowedPlants(){
        Userx userToBeSaved = new Userx();
        userToBeSaved.setUsername("user1");
        userService.saveUser(userToBeSaved);
        Plant plant1 = plantService.savePlant(new Plant());
        Plant plant2 = plantService.savePlant(new Plant());

        assertFalse(userService.loadUser("user1").getFollowedPlants().contains(plant1));

        userService.addPlantToFollowedPlants(userService.loadUser("user1"), plantService.loadPlant(plant1.getPlantID()));

        assertTrue(userService.loadUser("user1").getFollowedPlants().contains(plantService.loadPlant(plant1.getPlantID())));
        assertFalse(userService.loadUser("user1").getFollowedPlants().contains(plant2));

        userService.removePlantFromFollowedPlants(userService.loadUser("user1"), plantService.loadPlant(plant1.getPlantID()));

        assertFalse(userService.loadUser("user1").getFollowedPlants().contains(plant1));
    }


}