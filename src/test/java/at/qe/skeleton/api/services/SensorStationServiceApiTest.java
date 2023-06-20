package at.qe.skeleton.api.services;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.LogFrame;
import at.qe.skeleton.api.model.SensorApi;
import at.qe.skeleton.api.model.SensorDevice;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.api.model.SensorStationDataFrame;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.LogService;
import at.qe.skeleton.services.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SensorStationServiceApiTest {

    @Mock
    private SensorStationRepository sensorStationRepository;

    @Mock
    private SensorService sensorService;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private AccessPointService accessPointService;

    @Mock
    private IntervalService intervalService;

    @Mock
    private LogService logService;

    @InjectMocks
    private SensorStationServiceApi sensorStationServiceApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateSensorStation_ValidSensorStation_UpdateSuccessful() throws SensorStationNotFoundException {
        // Arrange
        SensorStationApi sensorStationApi = new SensorStationApi();
        sensorStationApi.setName("SensorStation1");
        sensorStationApi.setService_description("Description");
        sensorStationApi.setAlarm_switch("ON");

        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("SensorStation1");

        when(sensorStationRepository.findFirstById(anyString())).thenReturn(sensorStation);
        when(sensorStationRepository.save(any(SensorStation.class))).thenReturn(sensorStation);

        // Act
        assertDoesNotThrow(() -> sensorStationServiceApi.updateSensorStation(sensorStationApi));

        // Assert
        verify(sensorStationRepository, times(1)).findFirstById(eq("SensorStation1"));
        verify(sensorStationRepository, times(1)).save(eq(sensorStation));
        assertEquals("Description", sensorStation.getDescription());
        assertEquals("ON", sensorStation.getAlarmSwitch());
    }

    @Test
    void updateSensorStation_InvalidSensorStation_ThrowsException() {
        // Arrange
        SensorStationApi sensorStationApi = new SensorStationApi();
        sensorStationApi.setName("InvalidSensorStation");

        when(sensorStationRepository.findFirstById(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(SensorStationNotFoundException.class, () -> sensorStationServiceApi.updateSensorStation(sensorStationApi));

        // Verify
        verify(sensorStationRepository, times(1)).findFirstById(eq("InvalidSensorStation"));
        verify(sensorStationRepository, never()).save(any(SensorStation.class));
    }

    @Test
    void findOneSensorStation_ExistingSensorStation_ReturnsAlarmSwitch() throws SensorStationNotFoundException {
        // Arrange
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationName("SensorStation1");
        sensorStation.setAlarmSwitch("ON");

        when(sensorStationRepository.findFirstById(anyString())).thenReturn(sensorStation);

        // Act
        String alarmSwitch = sensorStationServiceApi.findOneSensorStation("SensorStation1");

        // Assert
        verify(sensorStationRepository, times(1)).findFirstById(eq("SensorStation1"));
        assertEquals("ON", alarmSwitch);
    }

    @Test
    void findOneSensorStation_NonexistentSensorStation_ThrowsException() {
        // Arrange
        when(sensorStationRepository.findFirstById(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(SensorStationNotFoundException.class, () -> sensorStationServiceApi.findOneSensorStation("InvalidSensorStation"));

        // Verify
        verify(sensorStationRepository, times(1)).findFirstById(eq("InvalidSensorStation"));
    }

    @Test
    void isValidated_ValidAccessPoint_ReturnsTrue() throws AccessPointNotFoundException {
        // Arrange
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setValidated(true);

        when(accessPointService.loadAccessPoint(anyLong())).thenReturn(accessPoint);

        // Act
        boolean isValidated = sensorStationServiceApi.isValidated(1L);

        // Assert
        verify(accessPointService, times(1)).loadAccessPoint(eq(1L));
        assertTrue(isValidated);
    }

    @Test
    void isValidated_InvalidAccessPoint_ThrowsException() {
        // Arrange
        when(accessPointService.loadAccessPoint(anyLong())).thenReturn(null);

        // Act & Assert
        assertThrows(AccessPointNotFoundException.class, () -> sensorStationServiceApi.isValidated(1L));

        // Verify
        verify(accessPointService, times(1)).loadAccessPoint(eq(1L));
    }

    @Test
    void findAllSensorStation_ExistingAccessPoint_ReturnsSensorStationNames() throws SensorStationNotFoundException {
        // Arrange
        List<SensorStation> sensorStations = new ArrayList<>();
        SensorStation sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationName("SensorStation1");
        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationName("SensorStation2");
        sensorStations.add(sensorStation1);
        sensorStations.add(sensorStation2);

        when(sensorStationRepository.findAllByAccessPoint_AccessPointID(anyLong())).thenReturn(sensorStations);

        // Act
        List<String> sensorStationNames = sensorStationServiceApi.findAllSensorStation(1L);

        // Assert
        verify(sensorStationRepository, times(1)).findAllByAccessPoint_AccessPointID(eq(1L));
        assertEquals(2, sensorStationNames.size());
        assertTrue(sensorStationNames.contains("SensorStation1"));
        assertTrue(sensorStationNames.contains("SensorStation2"));
    }

    @Test
    void findAllSensorStation_NonexistentAccessPoint_ThrowsException() {
        // Arrange
        when(sensorStationRepository.findAllByAccessPoint_AccessPointID(anyLong())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(SensorStationNotFoundException.class, () -> sensorStationServiceApi.findAllSensorStation(1L));

        // Verify
        verify(sensorStationRepository, times(1)).findAllByAccessPoint_AccessPointID(eq(1L));
    }

    @Test
    void addSensor_ValidSensorApiList_SensorsAddedSuccessfully() throws SensorNotFoundException {
        // Arrange
        List<SensorApi> sensorApiList = new ArrayList<>();
        SensorApi sensorApi1 = new SensorApi();
        sensorApi1.setStation_name("SensorStation1");
        SensorApi sensorApi2 = new SensorApi();
        sensorApi2.setStation_name("SensorStation2");
        sensorApiList.add(sensorApi1);
        sensorApiList.add(sensorApi2);

        SensorStation sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationName("SensorStation1");

        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationName("SensorStation2");

        when(sensorStationRepository.findFirstById(eq("SensorStation1"))).thenReturn(sensorStation1);
        when(sensorStationRepository.findFirstById(eq("SensorStation2"))).thenReturn(sensorStation2);


        // Act
        assertDoesNotThrow(() -> sensorStationServiceApi.addSensor(sensorApiList));

        // Assert
        verify(sensorStationRepository, times(1)).findFirstById(eq("SensorStation2"));
        verify(sensorService, times(2)).saveSensor(any(Sensor.class));
    }


    @Test
    void updateSensor_ValidSensorApiList_UpdateSuccessful() throws SensorStationNotFoundException {

        List<SensorApi> sensorApiList = new ArrayList<>();
        SensorApi sensorApi1 = new SensorApi();
        sensorApi1.setStation_name("SensorStation1");
        sensorApi1.setType("SensorType1");
        sensorApi1.setAlarm_count(5);
        SensorApi sensorApi2 = new SensorApi();
        sensorApi2.setStation_name("SensorStation2");
        sensorApi2.setType("SensorType2");
        sensorApi2.setAlarm_count(10);
        sensorApiList.add(sensorApi1);
        sensorApiList.add(sensorApi2);

        SensorStation sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationName("SensorStation1");

        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationName("SensorStation2");

        Sensor sensor1 = new Sensor();
        sensor1.setSensorStation(sensorStation1);
        sensor1.setType("SensorType1");

        Sensor sensor2 = new Sensor();
        sensor2.setSensorStation(sensorStation2);
        sensor2.setType("SensorType2");

        when(sensorStationRepository.findFirstById(eq("SensorStation1"))).thenReturn(sensorStation1);
        when(sensorStationRepository.findFirstById(eq("SensorStation2"))).thenReturn(sensorStation2);

        when(sensorService.getSensorForSensorStation(eq(sensorStation1), eq("SensorType1"))).thenReturn(sensor1);
        when(sensorService.getSensorForSensorStation(eq(sensorStation2), eq("SensorType2"))).thenReturn(sensor2);
        when(sensorService.saveSensor(any(Sensor.class))).thenReturn(null);

        // Act
        assertDoesNotThrow(() -> sensorStationServiceApi.updateSensor(sensorApiList));

        // Assert
        verify(sensorStationRepository, times(1)).findFirstById(eq("SensorStation2"));
        verify(sensorService, times(2)).getSensorForSensorStation(any(SensorStation.class), anyString());
        verify(sensorService, times(2)).saveSensor(any(Sensor.class));
        assertEquals(5, sensor1.getAlarm_count());
        assertEquals(10, sensor2.getAlarm_count());
    }

    @Test
    void updateSensor_InvalidSensorApiList_ThrowsException() {
        // Arrange
        List<SensorApi> sensorApiList = new ArrayList<>();
        SensorApi sensorApi = new SensorApi();
        sensorApi.setStation_name("InvalidSensorStation");
        sensorApi.setType("InvalidSensorType");
        sensorApi.setAlarm_count(5);
        sensorApiList.add(sensorApi);

        when(sensorStationRepository.findFirstById(eq("InvalidSensorStation"))).thenReturn(null);

        // Act & Assert
        assertThrows(SensorStationNotFoundException.class, () -> sensorStationServiceApi.updateSensor(sensorApiList));

        // Verify
        verify(sensorStationRepository, times(1)).findFirstById(eq("InvalidSensorStation"));
        verify(sensorService, never()).getSensorForSensorStation(any(SensorStation.class), anyString());
        verify(sensorService, never()).saveSensor(any(Sensor.class));
    }



    @Test
    void saveLog_ValidLogFrameList_LogsSavedSuccessfully() throws SensorStationNotFoundException {
        // Arrange
        List<LogFrame> logFrames = new ArrayList<>();
        LogFrame logFrame1 = new LogFrame();
        logFrame1.setAuthor("Author1");
        logFrame1.setTime_stamp("2023-06-14 10:00:00");
        logFrame1.setText("Log 1");
        logFrame1.setSubject("Subject 1");
        logFrame1.setType("ERROR");
        LogFrame logFrame2 = new LogFrame();
        logFrame2.setAuthor("Author2");
        logFrame2.setTime_stamp("2023-06-14 11:00:00");
        logFrame2.setText("Log 2");
        logFrame2.setSubject("Subject 2");
        logFrame2.setType("SUCCESS");
        logFrames.add(logFrame1);
        logFrames.add(logFrame2);

        when(logService.saveLog(any(Log.class))).thenReturn(null);

        // Act
        assertDoesNotThrow(() -> sensorStationServiceApi.saveLog(logFrames));

        // Assert
        verify(logService, times(2)).saveLog(any(Log.class));
    }
}
