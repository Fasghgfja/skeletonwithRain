package at.qe.skeleton.services;

import at.qe.skeleton.model.Sensor;
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
public class SensorServiceTest {

    @Autowired
    SensorService sensorService;

    @Autowired
    SensorStationService sensorStationService;

    /**
     * Testing the save() and getAllSensors() method of the SensorService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllSensors(){
        Sensor sensor = new Sensor();
        sensor.setId(1000L);
        sensor = sensorService.saveSensor(sensor);

        assertTrue(sensorService.getAllSensor().contains(sensor));
    }


    /**
     * Testing the getAllSensorsBySensorStation() and areSensorsPresent() method of the SensorService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAreSensorsPresent(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("SensorStation");
        sensorStation = sensorStationService.saveSensorStation(sensorStation);

        assertFalse(sensorService.areSensorsPresent(sensorStation));
        assertEquals(0, sensorService.getAllSensorsBySensorStation(sensorStation).size());

        Sensor sensor1 = new Sensor();
        sensor1.setId(100L);
        sensor1.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor1);
        Sensor sensor2 = new Sensor();
        sensor2.setId(101L);
        sensor2.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor2);
        Sensor sensor3 = new Sensor();
        sensor3.setId(102L);
        sensor3.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor3);
        Sensor sensor4 = new Sensor();
        sensor4.setId(103L);
        sensor4.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor4);
        Sensor sensor5 = new Sensor();
        sensor5.setId(104L);
        sensor5.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor5);
        Sensor sensor6 = new Sensor();
        sensor6.setId(105L);
        sensor6.setSensorStation(sensorStation);
        sensorService.saveSensor(sensor6);

        assertTrue(sensorService.areSensorsPresent(sensorStation));
        assertEquals(6, sensorService.getAllSensorsBySensorStation(sensorStation).size());
    }
}
