package at.qe.skeleton.beans;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.ui.beans.CreateAccessPointBean;
import at.qe.skeleton.ui.beans.CreatePlantBean;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreatePlantBeanTest {

    @Autowired
    private PlantService plantService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private LogRepository logRepository;

    private CreatePlantBean createPlantBean;

    @BeforeEach
    public void setUp() {
        createPlantBean = new CreatePlantBean();
        createPlantBean.setPlantService(plantService);
        createPlantBean.setSessionInfoBean(sessionInfoBean);
        createPlantBean.setLogRepository(logRepository);


    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoCreateNewAccessPoint() {
        assertNotNull(createPlantBean.getPlantService());
        assertNotNull(createPlantBean.getSessionInfoBean());
        assertNotNull(createPlantBean.getLogRepository());

        long initialPlantAmount = plantService.getPlantsAmount();
        long initialLogAmount = logRepository.count();
        createPlantBean.setPlantName("Plant");
        createPlantBean.setDescription("Description");
        assertNotNull(createPlantBean.getPlantName());
        assertNotNull(createPlantBean.getDescription());

        createPlantBean.doCreateNewPlant();

        Plant createdPlant = plantService.findFirstByName("Plant");
        assertNotNull(createdPlant);
        assertEquals("Plant", createdPlant.getPlantName());
        assertEquals("Description", createdPlant.getDescription());

        assertEquals(initialPlantAmount + 1, plantService.getPlantsAmount());
        assertEquals(initialLogAmount + 1, logRepository.count());
    }
}
