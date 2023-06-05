package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.LogService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccessPointControllerTest {

    private AccessPointController accessPointController;

    @Mock
    private AccessPointService accessPointService;

    @Mock
    private SensorStationService sensorStationService;

    @Mock
    private IntervalService intervalService;

    @Mock
    private LogService logService;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accessPointController = new AccessPointController();
        accessPointController.setAccessPointService(accessPointService);
        accessPointController.setSensorStationService(sensorStationService);
        accessPointController.setIntervalService(intervalService);
        accessPointController.setLogService(logService);
        accessPointController.setSessionInfoBean(sessionInfoBean);
    }

    @Test
    void testInit() {

        List<AccessPoint> accessPointList = new ArrayList<>();
        when(accessPointService.getAllAccessPoint()).thenReturn(accessPointList);


        accessPointController.init();


        assertEquals(accessPointList, accessPointController.getAccessPointList());
    }

    @Test
    void testGetAccessPoints() {

        Collection<AccessPoint> accessPoints = new ArrayList<>();
        when(accessPointService.getAllAccessPoint()).thenReturn(accessPoints);


        Collection<AccessPoint> result = accessPointController.getAccessPoints();


        assertEquals(accessPoints, result);
    }

    @Test
    void testGetAccessPointsAmount() {

        long expectedAmount = 5L;
        when(accessPointService.getAccessPointsAmount()).thenReturn(expectedAmount);


        long result = accessPointController.getAccessPointsAmount();


        assertEquals(expectedAmount, result);
    }

    @Test
    void testDoDeleteAccessPoint() {

        AccessPoint accessPoint = new AccessPoint();
        Log deleteLog = new Log();
        when(sessionInfoBean.getCurrentUserName()).thenReturn("TestUser");
        doNothing().when(intervalService).deleteIntervalByAccessPoint(accessPoint);
        doNothing().when(sensorStationService).removeAccessPointFromSensorStations(accessPoint);
        doNothing().when(accessPointService).deleteAccessPoint(accessPoint);
        when(logService.saveLog(any(Log.class))).thenReturn(deleteLog);


        accessPointController.setAccessPoint(accessPoint);
        accessPointController.doDeleteAccessPoint();


        verify(intervalService, times(1)).deleteIntervalByAccessPoint(accessPoint);
        verify(sensorStationService, times(1)).removeAccessPointFromSensorStations(accessPoint);
        verify(accessPointService, times(1)).deleteAccessPoint(accessPoint);
        assertNotNull(deleteLog.getDate());
        assertNotNull(deleteLog.getTime());
        assertEquals("TestUser", deleteLog.getAuthor());
        assertEquals("AP DELETION", deleteLog.getSubject());
        assertEquals("DELETED AP: " + accessPoint.getId(), deleteLog.getText());
        assertEquals(LogType.SUCCESS, deleteLog.getType());
    }

    @Test
    void testDoValidateAccessPoint() {

        AccessPoint accessPoint = new AccessPoint();
        Log validationLog = new Log();
        when(sessionInfoBean.getCurrentUserName()).thenReturn("TestUser");
        doNothing().when(accessPointService).saveAccessPoint(accessPoint);
        when(logService.saveLog(any(Log.class))).thenReturn(validationLog);


        accessPointController.setAccessPoint(accessPoint);
        accessPointController.doValidateAccessPoint();


        assertTrue(accessPoint.isValidated());
        verify(accessPointService, times(1)).saveAccessPoint(accessPoint);
        assertNotNull(validationLog.getDate());
        assertNotNull(validationLog.getTime());
        assertEquals("TestUser", validationLog.getAuthor());
        assertEquals("AP VALIDATED", validationLog.getSubject());
        assertEquals("VALIDATED AP: " + accessPoint.getId(), validationLog.getText());
        assertEquals(LogType.SUCCESS, validationLog.getType());
    }
}
