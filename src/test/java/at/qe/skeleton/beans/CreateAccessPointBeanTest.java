package at.qe.skeleton.beans;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.ui.beans.CreateAccessPointBean;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateAccessPointBeanTest {

    @Autowired
    private AccessPointService accessPointService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private LogRepository logRepository;

    private CreateAccessPointBean createAccessPointBean;

    @BeforeEach
    public void setUp() {
        createAccessPointBean = new CreateAccessPointBean();
        createAccessPointBean.setAccessPointService(accessPointService);
        createAccessPointBean.setSessionInfoBean(sessionInfoBean);
        createAccessPointBean.setLogRepository(logRepository);


    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoCreateNewAccessPoint() {
        assertNotNull(createAccessPointBean.getAccessPointService());
        assertNotNull(createAccessPointBean.getSessionInfoBean());
        assertNotNull(createAccessPointBean.getLogRepository());

        long initialLogAmount = logRepository.count();
        createAccessPointBean.setLocation("Location");
        assertNotNull(createAccessPointBean.getLocation());

        createAccessPointBean.doCreateNewAccessPoint();

        AccessPoint createdAccessPoint = accessPointService.getFirstById(1L);
        assertNotNull(createdAccessPoint);
        assertEquals("Location", createdAccessPoint.getLocation());
        assertFalse(createdAccessPoint.isValidated());

        assertEquals(initialLogAmount + 1, logRepository.count());
    }
}
