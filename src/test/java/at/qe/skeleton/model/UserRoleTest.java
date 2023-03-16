package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRoleTest {

    @Test
    void testValues() {
        // Ensure that the values of the enum are correct
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.GARDENER, UserRole.valueOf("GARDENER"));
        assertEquals(UserRole.USER, UserRole.valueOf("USER"));

        // Ensure that the values are returned to the correct order
        UserRole[] roles = UserRole.values();
        assertEquals(UserRole.ADMIN, roles[0]);
        assertEquals(UserRole.GARDENER, roles[1]);
        assertEquals(UserRole.USER, roles[2]);
    }


}
