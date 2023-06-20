package at.qe.skeleton.services;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.services.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private SensorService sensorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSensor() {
        // Arrange
        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        when(sensorRepository.findAll()).thenReturn(List.of(sensor1, sensor2));

        // Act
        Collection<Sensor> sensors = sensorService.getAllSensor();

        // Assert
        assertEquals(2, sensors.size());
        assertTrue(sensors.contains(sensor1));
        verify(sensorRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSensorsBySensorStation() {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        when(sensorRepository.findSensorsBySensorStation(sensorStation)).thenReturn(List.of(sensor1, sensor2));

        // Act
        Collection<Sensor> sensors = sensorService.getAllSensorsBySensorStation(sensorStation);

        // Assert
        assertEquals(2, sensors.size());
        assertTrue(sensors.contains(sensor1));

        verify(sensorRepository, times(1)).findSensorsBySensorStation(sensorStation);
    }

    @Test
    void testAreSensorsPresent() {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        when(sensorRepository.countSensors(sensorStation)).thenReturn(2);

        // Act
        boolean arePresent = sensorService.areSensorsPresent(sensorStation);

        // Assert
        assertTrue(arePresent);
        verify(sensorRepository, times(1)).countSensors(sensorStation);
    }

    @Test
    void testLoadSensor() {
        // Arrange
        Long id = 1L;
        Sensor sensor = new Sensor();
        when(sensorRepository.findFirstById(id)).thenReturn(sensor);

        // Act
        Sensor loadedSensor = sensorService.loadSensor(id);

        // Assert
        assertEquals(sensor, loadedSensor);
        verify(sensorRepository, times(1)).findFirstById(id);
    }

    @Test
    void testSaveSensor_NewSensor() {
        // Arrange
        Sensor sensor = new Sensor();
        when(sensorRepository.save(sensor)).thenReturn(sensor);

        // Act
        Sensor savedSensor = sensorService.saveSensor(sensor);

        // Assert
        assertNotNull(savedSensor.getCreateDate());
        assertNull(savedSensor.getUpdateDate());
        verify(sensorRepository, times(1)).save(sensor);
    }

    @Test
    void testSaveSensor_ExistingSensor() {
        // Arrange
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setCreateDate(LocalDate.now().minusDays(1));
        sensor.setUpdateDate(null);
        when(sensorRepository.save(sensor)).thenReturn(sensor);

        // Act
        Sensor savedSensor = sensorService.saveSensor(sensor);

        // Assert
        assertNotNull(savedSensor.getCreateDate());
        assertNotNull(savedSensor.getUpdateDate());
        assertEquals(LocalDate.now(), savedSensor.getUpdateDate());
        verify(sensorRepository, times(1)).save(sensor);
    }

    @Test
    void testGetSensorStatus_OK() {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        Sensor sensor = new Sensor();
        sensor.setSensorStation(sensorStation);
        sensor.setAlarm_count(1);
        when(sensorRepository.findFirstBySensorStationAndType(sensorStation, "TEMPERATURE")).thenReturn(sensor);

        // Act
        String status = sensorService.getSensorStatus("TEMPERATURE", sensorStation);

        // Assert
        assertEquals("Wrong", status);
        verify(sensorRepository, times(1)).findFirstBySensorStationAndType(sensorStation, "TEMPERATURE");
    }

    @Test
    void testGetSensorStatus_Wrong() {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        Sensor sensor = new Sensor();
        sensor.setSensorStation(sensorStation);
        sensor.setAlarm_count(5);
        when(sensorRepository.findFirstBySensorStationAndType(sensorStation, "TEMPERATURE")).thenReturn(sensor);

        // Act
        String status = sensorService.getSensorStatus("TEMPERATURE", sensorStation);

        // Assert
        assertEquals("Wrong", status);
        verify(sensorRepository, times(1)).findFirstBySensorStationAndType(sensorStation, "TEMPERATURE");
    }

    @Test
    void testGetSensorForSensorStation() {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        Sensor sensor = new Sensor();
        when(sensorRepository.findFirstBySensorStationAndType(sensorStation, "TEMPERATURE")).thenReturn(sensor);

        // Act
        Sensor result = sensorService.getSensorForSensorStation(sensorStation, "TEMPERATURE");

        // Assert
        assertEquals(sensor, result);
        verify(sensorRepository, times(1)).findFirstBySensorStationAndType(sensorStation, "TEMPERATURE");
    }

    @Test
    void testDeleteSensor() {
        // Arrange
        Sensor sensor = new Sensor();

        // Act
        sensorService.deleteSensor(sensor);

        // Assert
        verify(sensorRepository, times(1)).delete(sensor);
    }

    @Test
    void testGetSensorStationsAmount() {
        // Arrange
        when(sensorRepository.count()).thenReturn(5L);

        // Act
        long amount = sensorService.getSensorStationsAmount();

        // Assert
        assertEquals(5L, amount);
        verify(sensorRepository, times(1)).count();
    }
}
