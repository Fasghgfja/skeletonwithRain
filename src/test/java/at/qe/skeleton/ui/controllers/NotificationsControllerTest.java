package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(NotificationsController.class)
class NotificationsControllerTest {

    private NotificationsController notificationsController;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @Mock
    private SensorService sensorService;

    @Mock
    private ImageService imageService;

    @Mock
    private SensorStationService sensorStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationsController = new NotificationsController();
        notificationsController.setSessionInfoBean(sessionInfoBean);
        notificationsController.setSensorService(sensorService);
        notificationsController.setImageService(imageService);
        notificationsController.setSensorStationService(sensorStationService);
    }

    @Test
    void testGetAssignedSensorStationsAlarms_WithoutShowingAssignedAlarms() {
        notificationsController.setHasShownAssignedAlarms(false);
        SensorStation sensorStation1 = new SensorStation();
        SensorStation sensorStation2 = new SensorStation();
        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        Sensor sensor3 = new Sensor();
        sensor1.setSensorStation(sensorStation1);
        sensor2.setSensorStation(sensorStation1);
        sensor3.setSensorStation(sensorStation2);
        sensor1.setAlarm_count(2);
        sensor2.setAlarm_count(3);
        sensor3.setAlarm_count(1);
        List<SensorStation> sensorStationsUnderCare = new ArrayList<>();
        sensorStationsUnderCare.add(sensorStation1);
        sensorStationsUnderCare.add(sensorStation2);
        doReturn(sensorStationsUnderCare).when(sessionInfoBean.getCurrentUser()).getSensorStationsUnderCare();
        doReturn(Arrays.asList(sensor1, sensor2)).when(sensorService).getAllSensorsBySensorStation(sensorStation1);
        doReturn(Collections.singletonList(sensor3)).when(sensorService).getAllSensorsBySensorStation(sensorStation2);

        long result = notificationsController.getAssignedSensorStationsAlarms();

        assertEquals(2, result);
        assertTrue(notificationsController.isHasShownAssignedAlarms());
        verify(sessionInfoBean, times(1)).getCurrentUser().getSensorStationsUnderCare();
        verify(sensorService, times(1)).getAllSensorsBySensorStation(sensorStation1);
        verify(sensorService, times(1)).getAllSensorsBySensorStation(sensorStation2);
    }


    @Test
    void testGetAssignedSensorStationsAlarms_WithShowingAssignedAlarms() {
        notificationsController.setHasShownAssignedAlarms(true);
        SensorStation sensorStation1 = new SensorStation();
        SensorStation sensorStation2 = new SensorStation();
        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        Sensor sensor3 = new Sensor();
        sensor1.setSensorStation(sensorStation1);
        sensor2.setSensorStation(sensorStation1);
        sensor3.setSensorStation(sensorStation2);
        sensor1.setAlarm_count(2);
        sensor2.setAlarm_count(3);
        sensor3.setAlarm_count(1);

        long result = notificationsController.getAssignedSensorStationsAlarms();

        assertEquals(0, result);
        assertTrue(notificationsController.isHasShownAssignedAlarms());
        verifyNoInteractions(sessionInfoBean);
        verifyNoInteractions(sensorService);
    }

    @Test
    void testGetPicturesAwaitingApprovalCount_WithoutShowingPicsAwaitingApproval() {
        notificationsController.setHasShownPicsAwaitingApproval(false);
        Plant plant1 = new Plant();
        Plant plant2 = new Plant();
        SensorStation sensorStation1 = new SensorStation();
        SensorStation sensorStation2 = new SensorStation();
        sensorStation1.setPlant(plant1);
        sensorStation2.setPlant(plant2);
        List<SensorStation> sensorStationsUnderCare = new ArrayList<>();
        sensorStationsUnderCare.add(sensorStation1);
        sensorStationsUnderCare.add(sensorStation2);
        doReturn(sensorStationsUnderCare).when(sessionInfoBean.getCurrentUser()).getSensorStationsUnderCare();
        doReturn(2L).when(imageService).getNotApprovedImagesAmountForPlant(plant1);
        doReturn(3L).when(imageService).getNotApprovedImagesAmountForPlant(plant2);

        long result = notificationsController.getPicturesAwaitingApprovalCount();

        assertEquals(5, result);
        assertTrue(notificationsController.isHasShownPicsAwaitingApproval());
        verify(sessionInfoBean, times(1)).getCurrentUser().getSensorStationsUnderCare();
        verify(imageService, times(1)).getNotApprovedImagesAmountForPlant(plant1);
        verify(imageService, times(1)).getNotApprovedImagesAmountForPlant(plant2);
    }


    @Test
    void testGetPicturesAwaitingApprovalCount_WithShowingPicsAwaitingApproval() {
        notificationsController.setHasShownPicsAwaitingApproval(true);

        long result = notificationsController.getPicturesAwaitingApprovalCount();

        assertEquals(0, result);
        assertTrue(notificationsController.isHasShownPicsAwaitingApproval());
        verifyNoInteractions(sessionInfoBean);
        verifyNoInteractions(imageService);
    }
}

