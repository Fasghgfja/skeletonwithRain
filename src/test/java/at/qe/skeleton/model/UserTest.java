package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the model for user: Userx
 */
class UserTest {

    private final Userx user = new Userx();

    /**
     * Testing getter and setter for username / id.
     */
    @Test
    void testUsername() {
        user.setUsername("test_user");
        assertEquals("test_user", user.getUsername());
        user.setId("user1");
        assertEquals("user1", user.getId());
    }

    /**
     * Testing getter and setter for password.
     */
    @Test
    void testPassword() {
        user.setPassword("test_password");
        assertEquals("test_password", user.getPassword());
    }

    /**
     * Testing getter and setter for firstName.
     */
    @Test
    void testFirstName() {
        user.setFirstName("Test");
        assertEquals("Test", user.getFirstName());
    }

    /**
     * Testing getter and setter for lastName.
     */
    @Test
    void testLastName() {
        user.setLastName("User");
        assertEquals("User", user.getLastName());
    }

    /**
     * Testing getter and setter for email.
     */
    @Test
    void testEmail() {
        user.setEmail("test_user@example.com");
        assertEquals("test_user@example.com", user.getEmail());
    }

    /**
     * Testing getter and setter for phone.
     */
    @Test
    void testPhone() {
        user.setPhone("06642333142");
        assertEquals("06642333142", user.getPhone());
    }

    /**
     * Testing getter and setter for enabled.
     */
    @Test
    void testEnabled() {
        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    /**
     * Testing the getter and setter for the field roles of the Userx model.
     */
    @Test
    void testRoles(){
        Userx user = new Userx();
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ADMIN);
        roles.add(UserRole.GARDENER);
        roles.add(UserRole.USER);

        assertNull(user.getRoles());
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    /**
     * Testing toString() method of the Userx model.
     */
    @Test
    void testToString(){
        Userx user = new Userx();
        String username = "user";
        user.setUsername(username);
        assertEquals(username, user.toString());
    }

    /**
     * Testing compareTo() method of the Userx model.
     */
    @Test
    void testCompareTo(){
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");

        assertTrue(user1.compareTo(user2) < 0);
        assertTrue(user2.compareTo(user1) > 0);
    }

    /**
     * Testing getter and setter for field updateUser of model Userx.
     */
    @Test
    void testUpdateUser(){
        Userx user = new Userx();
        assertNull(user.getUpdateUser());
        user.setUpdateUser(user);
        assertEquals(user, user.getUpdateUser());
    }

    /**
     * Testing getter and setter for field profilePic of model Userx.
     */
    @Test
    void testProfilePic(){
        Image image = new Image();
        Userx user = new Userx();
        assertNull(user.getProfilePic());
        user.setProfilePic(image);
        assertEquals(image, user.getProfilePic());
    }


}
