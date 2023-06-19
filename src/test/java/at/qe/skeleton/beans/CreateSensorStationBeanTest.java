package at.qe.skeleton.beans;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.CreateSensorStationBean;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateSensorStationBeanTest {

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private LogRepository logRepository;

    private CreateSensorStationBean createSensorStationBean;

    @BeforeEach
    public void setUp() {
        createSensorStationBean = new CreateSensorStationBean();
        createSensorStationBean.setSensorStationService(sensorStationService);
        createSensorStationBean.setSessionInfoBean(sessionInfoBean);
        createSensorStationBean.setLogRepository(logRepository);

        createSensorStationBean.setLocation("Location");
        createSensorStationBean.setSensorStationName("SensorStation");
        createSensorStationBean.setAlarmSwitch("Off");
        createSensorStationBean.setDescription("Description");
        createSensorStationBean.setAlarmCountThreshold(5);
    }

    /*
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoCreateNewAccessPoint() {
        assertNotNull(createSensorStationBean.getSensorStationService());
        assertNotNull(createSensorStationBean.getSessionInfoBean());
        assertNotNull(createSensorStationBean.getLogRepository());

        long initialPlantAmount = sensorStationService.getSensorStationsAmount();
        long initialLogAmount = logRepository.count();

        assertNotNull(createSensorStationBean.getLocation());
        assertNotNull(createSensorStationBean.getSensorStationName());
        assertNotNull(createSensorStationBean.getAlarmSwitch());
        assertNotNull(createSensorStationBean.getDescription());
        assertNotNull(createSensorStationBean.getAlarmCountThreshold());

        createSensorStationBean.doCreateNewCreateSensorStation();

        SensorStation createdSensorStation = sensorStationService.loadSensorStation("SensorStation");
        assertNotNull(createdSensorStation);
        assertEquals("Location", createdSensorStation.getLocation());
        assertEquals("Description", createdSensorStation.getDescription());
        assertEquals("SensorStation", createdSensorStation.getSensorStationName());
        assertEquals("off", createdSensorStation.getAlarmSwitch());
        assertEquals(5, createdSensorStation.getAlarmCountThreshold());

        assertEquals(initialPlantAmount + 1, sensorStationService.getSensorStationsAmount());
        assertEquals(initialLogAmount + 1, logRepository.count());
    }

     */
}
