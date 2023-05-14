package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorStation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
class SensorStationServiceTest {

    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    UserService userService;

    /**
     * Method to test the getAllSensorStations() method of the sensorStationService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensorStations(){
        int initialAmountOfSensorStations = sensorStationService.getAllSensorStations().size();

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSensorStation");
        sensorStationService.saveSensorStation(sensorStation);

        assertEquals(initialAmountOfSensorStations + 1, sensorStationService.getAllSensorStations().size());
    }

    /**
     * Testing the getAllAssignedSensorStations(Userx user) of the sensorStationService.
     * This method will also use the addGardenerToSensorStation()
     * and removeGardenerFromSensorStation() method.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllAssignedSensorStations(){
       //TODO: clean these methods in the service.
    }

    /**
     * Testing loadSensorStation(String id) of the sensorStationService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadSensorStation(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSensorStation");
        sensorStationService.saveSensorStation(sensorStation);

        assertEquals(sensorStation, sensorStationService.loadSensorStation("TestSensorStation"));
    }

    /**
     * Testing the deleteSensorStation(SensorStation sensorStation) method of the sensorStationService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteSensorStation(){
        int initialValue = sensorStationService.getAllSensorStations().size();
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSensorStation");
        sensorStationService.saveSensorStation(sensorStation);
        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationID("TestSensorStation2");
        sensorStationService.saveSensorStation(sensorStation2);

        assertEquals(initialValue + 2, sensorStationService.getAllSensorStations().size());

        sensorStationService.deleteSensorStation(sensorStation);
        assertEquals(initialValue + 1, sensorStationService.getAllSensorStations().size());
        sensorStationService.deleteSensorStation(sensorStation2);
        assertEquals(initialValue, sensorStationService.getAllSensorStations().size());
    }

    /**
     * Testing the getSensorStationAmount() method of the sensorStationService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetSensorStationsAmount(){
        int initialValue = sensorStationService.getAllSensorStations().size();

        assertEquals(initialValue, sensorStationService.getSensorStationsAmount());

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSensorStation");
        sensorStationService.saveSensorStation(sensorStation);
        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationID("TestSensorStation2");
        sensorStationService.saveSensorStation(sensorStation2);

        assertEquals(initialValue + 2, sensorStationService.getSensorStationsAmount());
    }

    /**
     * Testing the getAllSensorStationIds() method of the sensorStationSerive.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensorStationsIds(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSensorStation");
        sensorStationService.saveSensorStation(sensorStation);

        assertTrue(sensorStationService.getAllSensorStationsIds().contains(sensorStation.getSensorStationID()));
        sensorStationService.deleteSensorStation(sensorStationService.loadSensorStation(sensorStation.getSensorStationID()));

        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationID("TestSensorStation2");
        sensorStationService.saveSensorStation(sensorStation2);

        assertFalse(sensorStationService.getAllSensorStationsIds().contains(sensorStation.getSensorStationID()));
        assertTrue(sensorStationService.getAllSensorStationsIds().contains(sensorStation2.getSensorStationID()));
    }
}

