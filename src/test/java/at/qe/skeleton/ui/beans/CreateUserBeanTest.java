package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.UserListController;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserBeanTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserxRepository userxRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @Mock
    private UserListController userListController;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    private CreateUserBean createUserBean;

    @BeforeEach
    void setUp() {
        createUserBean = new CreateUserBean();
        createUserBean.setUserService(userService);
        createUserBean.setPasswordEncoder(passwordEncoder);
        createUserBean.setUserxRepository(userxRepository);
        createUserBean.setLogRepository(logRepository);
        createUserBean.setSessionInfoBean(sessionInfoBean);
        createUserBean.setUserListController(userListController);
        createUserBean.setSelectedRoles(Arrays.asList("ADMIN", "GARDENER"));
        createUserBean.setUsername("testuser");
        createUserBean.setPassword("testpassword");
        createUserBean.setEmail("test@example.com");
        createUserBean.setFirstName("Test");
        createUserBean.setLastName("User");
        createUserBean.setPhone("1234567890");

        when(facesContext.getExternalContext()).thenReturn(externalContext);
    }


    @Test
    void testRegisterNewUser() throws Exception {
        try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
            mocked.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            // when username is already taken
            when(userxRepository.findFirstByUsername(anyString())).thenReturn(new Userx());

            createUserBean.registerNewUser();
            verify(logRepository).save(any(Log.class));
            verify(userService, never()).saveUser(any(Userx.class));
            verify(externalContext).redirect(anyString());

            // Resetting interactions for the next scenario
            reset(userxRepository, userService, logRepository, externalContext);

            // when username is not taken
            when(userxRepository.findFirstByUsername(anyString())).thenReturn(null);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
            when(sessionInfoBean.getCurrentUserName()).thenReturn("admin");

            createUserBean.registerNewUser();
            verify(userService).saveUser(any(Userx.class));
            verify(logRepository).save(any(Log.class)); // count increased due to successful registration
            verify(externalContext).redirect(anyString());
        }
    }
}
