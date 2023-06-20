package at.qe.skeleton.beans;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.CreateSensorStationBean;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateSensorStationBeanTest {

    @Mock
    private SensorStationService sensorStationService;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @Mock
    private IntervalService intervalService;

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private CreateSensorStationBean createSensorStationBean;

    private Logger successLogger;
    private FileHandler successFileHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        successLogger = Logger.getLogger("SuccessLogger");
    }

    @Test
    void testDoCreateNewCreateSensorStation() throws IOException {
        // Arrange
        String location = "Location";
        String sensorStationName = "Sensor Station";
        String description = "Description";
        Integer alarmCountThreshold = 5;
        SensorStation sensorStation = new SensorStation();
        sensorStation.setLocation(location);
        sensorStation.setSensorStationName(sensorStationName);
        sensorStation.setAlarmSwitch("off");
        sensorStation.setDescription(description);
        sensorStation.setAlarmCountThreshold(alarmCountThreshold);
        when(sensorStationService.saveSensorStation(any(SensorStation.class))).thenReturn(sensorStation);

        SSInterval interval = new SSInterval();
        interval.setWebAppInterval("1");
        interval.setMeasurementInterval("1");
        interval.setSensorStation(sensorStation);
        when(intervalService.saveInterval(any(SSInterval.class))).thenReturn(interval);

        when(logRepository.save(any(Log.class))).thenReturn(null);

        Userx currentUser = new Userx();
        currentUser.setUsername("admin"); // Set the username of the current user
        when(sessionInfoBean.getCurrentUser()).thenReturn(currentUser);

        // Act
        createSensorStationBean.setLocation(location);
        createSensorStationBean.setSensorStationName(sensorStationName);
        createSensorStationBean.setAlarmSwitch("off");
        createSensorStationBean.setDescription(description);
        createSensorStationBean.setAlarmCountThreshold(alarmCountThreshold);
        createSensorStationBean.doCreateNewCreateSensorStation();

        // Assert
        verify(sensorStationService, times(1)).saveSensorStation(any(SensorStation.class));
        verify(intervalService, times(1)).saveInterval(any(SSInterval.class));
        verify(logRepository, times(1)).save(any(Log.class));
        assertEquals(location, sensorStation.getLocation());
        assertEquals(sensorStationName, sensorStation.getSensorStationName());
        assertEquals("off", sensorStation.getAlarmSwitch());
        assertEquals(description, sensorStation.getDescription());
        assertEquals(alarmCountThreshold, sensorStation.getAlarmCountThreshold());
    }
}

