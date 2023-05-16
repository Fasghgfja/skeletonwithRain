package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing the model for user: Userx
 */
class UserTest {

    private final Userx user = new Userx();

    /**
     * Testing getter and setter for username.
     */
    @Test
    void testUsername() {
        user.setUsername("test_user");
        assertEquals("test_user", user.getUsername());
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




}
