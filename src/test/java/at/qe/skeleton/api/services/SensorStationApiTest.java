package at.qe.skeleton.api.services;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class SensorStationApiTest {
    @Autowired
    SensorStationServiceApi sensorStationServiceApi;
    @Autowired
    SensorStationRepository sensorStationRepository;
    @DirtiesContext
    @Test
    //@WithMockUser(username = "admin", authorities = {"passwd"})
    void testCallSensorboardersByAccesspointID() throws SensorStationNotFoundException {
        Long accsessPointID = Long.valueOf("50100");
        //Assertions.assertEquals(1, sensorStationRepository.findAllByAccessPoint_AccessPointID(accsessPointID).size());
    }
}
