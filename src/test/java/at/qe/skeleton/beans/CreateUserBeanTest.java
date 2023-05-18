package at.qe.skeleton.beans;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.CreateUserBean;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the CreateUserBean.
 */
@SpringBootTest
class CreateUserBeanTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserxRepository userxRepository;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    LogRepository logRepository;

    private CreateUserBean createUserBean;

    @BeforeEach
    void setUp(){
        createUserBean = new CreateUserBean();
        createUserBean.setUserService(userService);
        createUserBean.setPasswordEncoder(passwordEncoder);
        createUserBean.setUserxRepository(userxRepository);
        createUserBean.setLogRepository(logRepository);
        createUserBean.setSessionInfoBean(sessionInfoBean);

        List<String> selectedRoles = new ArrayList<>();
        Set<UserRole> roles = new HashSet<>();

        createUserBean.setUsername("username");
        createUserBean.setFirstName("FirstName");
        createUserBean.setLastName("LastName");
        createUserBean.setPassword("Password");
        createUserBean.setEmail("Email");
        createUserBean.setPhone("Phone");
        createUserBean.setSelectedRoles(selectedRoles);
        createUserBean.setRoles(roles);
    }

    /**
     * This method tests the createNewUser() method of the CreateUserBean.
     * First a user is created with the given parameters initialized in the setUp() method.
     * Afterward we create another user with the same parameters. This should not be possible.
     * Therefore, only the amount of logs increases but not the user count.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateNewUser(){
        assertNotNull(createUserBean.getUserService());
        assertNotNull(createUserBean.getPasswordEncoder());
        assertNotNull(createUserBean.getUserxRepository());
        assertNotNull(createUserBean.getLogRepository());
        assertNotNull(createUserBean.getSessionInfoBean());
        assertNotNull(createUserBean.getSelectedRoles());
        assertNotNull(createUserBean.getRoles());
        assertNotNull(createUserBean.getUsername());
        assertNotNull(createUserBean.getFirstName());
        assertNotNull(createUserBean.getLastName());
        assertNotNull(createUserBean.getPassword());
        assertNotNull(createUserBean.getEmail());
        assertNotNull(createUserBean.getPhone());

        Integer initialAmountOfUsers = userxRepository.count();
        long initialAmountOfLogs = logRepository.count();

        createUserBean.createNewUser();

        assertEquals(initialAmountOfLogs + 1, logRepository.count());
        assertEquals(initialAmountOfUsers + 1, userxRepository.count());

        createUserBean.createNewUser();

        assertEquals(initialAmountOfLogs + 2, logRepository.count());
        assertEquals(initialAmountOfUsers + 1, userxRepository.count());
    }
}
