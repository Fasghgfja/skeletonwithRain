package at.qe.skeleton.tests;

import at.qe.skeleton.model.Userx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;

/**
 * Testing of the SessionInfoBean.
 */
@SpringBootTest
@WebAppConfiguration
class SessionInfoBeanTest {

    @Autowired
    SessionInfoBean sessionInfoBean;
    @Autowired
    UserService userService;

    /**
     * Testing if a user is logged in.
     * Additionally tests getCurrentUser(), getCurrentUserName(),
     * getCurrentUserRoles() and hasRole() of the SessionInfoBean.
     */
    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    void testLoggedIn() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        userService.saveUser(user1);

        Assertions.assertTrue(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does not return true for authenticated user");
        Assertions.assertEquals("user1", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return authenticated user's name");
        Assertions.assertEquals("user1", sessionInfoBean.getCurrentUser().getUsername(), "sessionInfoBean.getCurrentUser does not return authenticated user");
        Assertions.assertEquals("USER", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return authenticated user's roles");
        Assertions.assertTrue(sessionInfoBean.hasRole("USER"), "sessionInfoBean.hasRole does not return true for a role the authenticated user has");
        Assertions.assertFalse(sessionInfoBean.hasRole("ADMIN"), "sessionInfoBean.hasRole does not return false for a role the authenticated user does not have");
    }

    /**
     * Testing notLoggedIn() method of SessionInfoBean.
     */
    @Test
    void testNotLoggedIn() {
        Assertions.assertFalse(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does return true for not authenticated user");
        Assertions.assertEquals("", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return empty string when not logged in");
        Assertions.assertNull(sessionInfoBean.getCurrentUser(), "sessionInfoBean.getCurrentUser does not return null when not logged in");
        Assertions.assertEquals("", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return empty string when not logged in");
        for (UserRole role : UserRole.values()) {
            Assertions.assertFalse(sessionInfoBean.hasRole(role.name()), "sessionInfoBean.hasRole does not return false for all possible roales");
        }
    }

}