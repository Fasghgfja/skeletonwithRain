package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PairingControllerTest {

    private PairingController pairingController;

    @Mock
    private SensorStationService sensorStationService;
    @Mock
    private AccessPointService accessPointService;
    @Mock
    private SensorService sensorService;
    @Mock
    private SessionInfoBean sessionInfoBean;
    @Mock
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pairingController = new PairingController();
        pairingController.setSensorStationService(sensorStationService);
        pairingController.setAccessPointService(accessPointService);
        pairingController.setSensorService(sensorService);
        pairingController.setSessionInfoBean(sessionInfoBean);
        pairingController.setLogRepository(logRepository);
    }

    @Test
    void testPair_WithSensorsFound() {
        SensorStation sensorStation = new SensorStation();
        AccessPoint accessPoint = new AccessPoint();
        sensorStation.setSensorStationID("sensorStation1");
        accessPoint.setAccessPointID(1L);

        pairingController.setSensorStationId("sensorStation1");
        pairingController.setAccessPointId(1L);
        pairingController.setInterruptFlag(false);
        pairingController.setSensorsFound(true);

        pairingController.pair();

        verify(sensorStationService, times(1)).loadSensorStation("sensorStation1");
        verify(accessPointService, times(1)).loadAccessPoint(1L);
        verify(sensorStationService, times(1)).saveSensorStation(sensorStation);

        assertFalse(pairingController.isInterruptFlag());
    }

    @Test
    void testPair_WithoutSensorsFound() throws InterruptedException {
        SensorStation sensorStation = new SensorStation();
        AccessPoint accessPoint = new AccessPoint();
        sensorStation.setSensorStationID("sensorStation1");
        accessPoint.setAccessPointID(1L);

        List<SensorStation> sensorStationsUnderCare = new ArrayList<>();
        sensorStationsUnderCare.add(sensorStation);
        when(sensorStationService.loadSensorStation("sensorStation1")).thenReturn(sensorStation);
        when(accessPointService.loadAccessPoint(1L)).thenReturn(accessPoint);
        when(sensorService.areSensorsPresent(sensorStation)).thenReturn(false, true);
        when(sessionInfoBean.getCurrentUser()).thenReturn(null);
        when(logRepository.save(any(Log.class))).thenReturn(null);

        pairingController.setSensorStationId("sensorStation1");
        pairingController.setAccessPointId(1L);
        pairingController.setInterruptFlag(false);
        pairingController.setSensorsFound(false);

        long startTime = System.currentTimeMillis();
        pairingController.pair();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        verify(sensorStationService, times(1)).loadSensorStation("sensorStation1");
        verify(accessPointService, times(1)).loadAccessPoint(1L);
        verify(sensorStationService, times(1)).saveSensorStation(sensorStation);
        verify(sensorService, times(2)).areSensorsPresent(sensorStation);
        verify(sessionInfoBean, times(2)).getCurrentUser();
        verify(logRepository, times(1)).save(any(Log.class));

        assertTrue(executionTime >= 5000);
        assertTrue(pairingController.isInterruptFlag());
    }

    @Test
    void testUnpair() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("sensorStation1");

        when(sensorStationService.loadSensorStation("sensorStation1")).thenReturn(sensorStation);
        when(logRepository.save(any(Log.class))).thenReturn(null);

        pairingController.setSensorStationId("sensorStation1");

        pairingController.unpair();

        verify(sensorStationService, times(1)).loadSensorStation("sensorStation1");
        verify(sensorStationService, times(1)).saveSensorStation(sensorStation);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testAbortPairing_WithoutSensorsFound() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("sensorStation1");

        when(sensorStationService.loadSensorStation("sensorStation1")).thenReturn(sensorStation);

        pairingController.setSensorStationId("sensorStation1");
        pairingController.setSensorsFound(false);

        pairingController.abortPairing();

        verify(sensorStationService, times(1)).loadSensorStation("sensorStation1");
        verify(sensorStationService, times(1)).saveSensorStation(sensorStation);

        assertTrue(pairingController.isInterruptFlag());
    }

    @Test
    void testAbortPairing_WithSensorsFound() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("sensorStation1");

        pairingController.setSensorStationId("sensorStation1");
        pairingController.setSensorsFound(true);

        pairingController.abortPairing();

        verify(sensorStationService, never()).loadSensorStation(anyString());
        verify(sensorStationService, never()).saveSensorStation(any(SensorStation.class));

        assertTrue(pairingController.isInterruptFlag());
    }
}
