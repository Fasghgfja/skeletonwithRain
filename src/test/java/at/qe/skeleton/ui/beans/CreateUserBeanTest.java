package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.UserListController;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class CreateUserBeanTest {

    private CreateUserBean createUserBean;

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

    @BeforeEach
    void setUp() {
        createUserBean = new CreateUserBean();
        createUserBean.setUserService(userService);
        createUserBean.setPasswordEncoder(passwordEncoder);
        createUserBean.setUserxRepository(userxRepository);
        createUserBean.setLogRepository(logRepository);
        createUserBean.setSessionInfoBean(sessionInfoBean);
        createUserBean.setUserListController(userListController);
    }



    @Test
    void registerNewUser_UsernameTaken() throws IOException {
        // Mock dependencies
        UserxRepository userxRepository = mock(UserxRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        SessionInfoBean sessionInfoBean = mock(SessionInfoBean.class);
        LogRepository logRepository = mock(LogRepository.class);
        UserService userService = mock(UserService.class); // Create a mock UserService

        when(userxRepository.findFirstByUsername(anyString())).thenReturn(new Userx());

        // Create the instance of the class under test and set the dependencies
        CreateUserBean createUserBean = new CreateUserBean();
        createUserBean.setUserxRepository(userxRepository);
        createUserBean.setPasswordEncoder(passwordEncoder);
        createUserBean.setSessionInfoBean(sessionInfoBean);
        createUserBean.setLogRepository(logRepository);
        createUserBean.setUserService(userService); // Set the UserService

        // Mock FacesContext
        try (MockedStatic<FacesContext> mockedFacesContext = mockStatic(FacesContext.class)) {
            FacesContext facesContext = mock(FacesContext.class);
            ExternalContext externalContext = mock(ExternalContext.class);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            mockedFacesContext.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            // Call the method
            createUserBean.registerNewUser();

            // Verify that the appropriate actions were performed
            // For example, verify that a warning log was created
            verify(logRepository).save(any(Log.class));
            // Verify that the user is redirected to the login page
            verify(externalContext).redirect(contains("/login.xhtml"));
        }
    }




    @Test
    void registerNewUser_Success() throws IOException {
        // Mock dependencies
        UserxRepository userxRepository = mock(UserxRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        SessionInfoBean sessionInfoBean = mock(SessionInfoBean.class);
        LogRepository logRepository = mock(LogRepository.class);
        UserService userService = mock(UserService.class);
        ExternalContext externalContext = mock(ExternalContext.class);

        when(userxRepository.findFirstByUsername(anyString())).thenReturn(null);

        // Create the instance of the class under test and set the dependencies
        CreateUserBean createUserBean = new CreateUserBean();
        createUserBean.setUserxRepository(userxRepository);
        createUserBean.setPasswordEncoder(passwordEncoder);
        createUserBean.setSessionInfoBean(sessionInfoBean);
        createUserBean.setLogRepository(logRepository);
        createUserBean.setUserService(userService);

        // Mock FacesContext and ExternalContext
        try (MockedStatic<FacesContext> mocked = mockStatic(FacesContext.class)) {
            FacesContext facesContext = mock(FacesContext.class);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            mocked.when(FacesContext::getCurrentInstance).thenReturn(facesContext);

            // Call the method
            createUserBean.registerNewUser();

            // Verify that the appropriate actions were performed
            // For example, verify that a success log was created
            verify(logRepository).save(any(Log.class));
            // Verify that the user was saved
            verify(userService).saveUser(any(Userx.class));
            // Verify that the user is redirected to the login page
            verify(externalContext).redirect(contains("/login.xhtml"));
        }
    }

}
