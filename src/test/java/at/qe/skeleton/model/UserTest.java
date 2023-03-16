package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private final Userx user = new Userx();

    @Test
    void testUsername() {
        user.setUsername("test_user");
        assertEquals("test_user", user.getUsername());
    }

    @Test
    void testPassword() {
        user.setPassword("test_password");
        assertEquals("test_password", user.getPassword());
    }

    @Test
    void testFirstName() {
        user.setFirstName("Test");
        assertEquals("Test", user.getFirstName());
    }

    @Test
    void testLastName() {
        user.setLastName("User");
        assertEquals("User", user.getLastName());
    }

    @Test
    void testEmail() {
        user.setEmail("test_user@example.com");
        assertEquals("test_user@example.com", user.getEmail());
    }

    @Test
    void testEnabled() {
        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }




}
