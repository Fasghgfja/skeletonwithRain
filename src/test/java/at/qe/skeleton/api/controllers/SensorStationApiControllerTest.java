package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.LogFrame;
import at.qe.skeleton.api.model.SensorApi;
import at.qe.skeleton.api.model.SensorDevice;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.api.model.SensorStationDataFrame;
import at.qe.skeleton.api.services.SensorStationServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorStationApiControllerTest {

    @Mock
    private SensorStationServiceApi sensorStationServiceApi;

    @InjectMocks
    private SensorStationApiController sensorStationApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSensorStationApi_ValidSensorStation_ReturnsOkStatus() throws SensorStationNotFoundException {
        SensorStationApi sensorStationApi = new SensorStationApi();
        doNothing().when(sensorStationServiceApi).updateSensorStation(sensorStationApi);

        int result = sensorStationApiController.createSensorStationApi(sensorStationApi);

        assertEquals(HttpStatus.OK.value(), result);
        verify(sensorStationServiceApi, times(1)).updateSensorStation(sensorStationApi);
    }

    @Test
    void createSensorStationApi_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        SensorStationApi sensorStationApi = new SensorStationApi();
        doThrow(new SensorStationNotFoundException()).when(sensorStationServiceApi).updateSensorStation(sensorStationApi);

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.createSensorStationApi(sensorStationApi));
        verify(sensorStationServiceApi, times(1)).updateSensorStation(sensorStationApi);
    }

    @Test
    void createSensorApi_ValidSensorList_ReturnsOkStatus() throws SensorNotFoundException {
        List<SensorApi> sensorList = Collections.singletonList(new SensorApi());
        doNothing().when(sensorStationServiceApi).addSensor(sensorList);

        int result = sensorStationApiController.createSensorApi(sensorList);

        assertEquals(HttpStatus.OK.value(), result);
        verify(sensorStationServiceApi, times(1)).addSensor(sensorList);
    }

    @Test
    void createSensorApi_SensorNotFoundException_ThrowsResponseStatusException() throws SensorNotFoundException {
        List<SensorApi> sensorList = Collections.singletonList(new SensorApi());
        doThrow(new SensorNotFoundException()).when(sensorStationServiceApi).addSensor(sensorList);

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.createSensorApi(sensorList));
        verify(sensorStationServiceApi, times(1)).addSensor(sensorList);
    }

    @Test
    void updateSensorApi_ValidSensorList_ReturnsOkStatus() throws SensorStationNotFoundException {
        List<SensorApi> sensorList = Collections.singletonList(new SensorApi());
        doNothing().when(sensorStationServiceApi).updateSensor(sensorList);

        int result = sensorStationApiController.updateSensorApi(sensorList);

        assertEquals(HttpStatus.OK.value(), result);
        verify(sensorStationServiceApi, times(1)).updateSensor(sensorList);
    }

    @Test
    void updateSensorApi_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        List<SensorApi> sensorList = Collections.singletonList(new SensorApi());
        doThrow(new SensorStationNotFoundException()).when(sensorStationServiceApi).updateSensor(sensorList);

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.updateSensorApi(sensorList));
        verify(sensorStationServiceApi, times(1)).updateSensor(sensorList);
    }

    @Test
    void logFile_ValidLogFrameList_ReturnsOkStatus() throws SensorStationNotFoundException {
        List<LogFrame> logFrameList = Collections.singletonList(new LogFrame());
        doNothing().when(sensorStationServiceApi).saveLog(logFrameList);

        int result = sensorStationApiController.logFile(logFrameList);

        assertEquals(HttpStatus.OK.value(), result);
        verify(sensorStationServiceApi, times(1)).saveLog(logFrameList);
    }

    @Test
    void logFile_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        List<LogFrame> logFrameList = Collections.singletonList(new LogFrame());
        doThrow(new SensorStationNotFoundException()).when(sensorStationServiceApi).saveLog(logFrameList);

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.logFile(logFrameList));
        verify(sensorStationServiceApi, times(1)).saveLog(logFrameList);
    }

    @Test
    void nearbyDevices_ValidParameters_ReturnsOkStatus() throws AccessPointNotFoundException {
        String id = "1";
        List<SensorDevice> devices = Collections.singletonList(new SensorDevice());
        doNothing().when(sensorStationServiceApi).saveNearbyDevices(Long.valueOf(id), devices);

        int result = sensorStationApiController.nearbyDevices(id, devices);

        assertEquals(HttpStatus.OK.value(), result);
        verify(sensorStationServiceApi, times(1)).saveNearbyDevices(Long.valueOf(id), devices);
    }

    @Test
    void nearbyDevices_AccessPointNotFoundException_ThrowsResponseStatusException() throws AccessPointNotFoundException {
        String id = "1";
        List<SensorDevice> devices = Collections.singletonList(new SensorDevice());
        doThrow(new AccessPointNotFoundException()).when(sensorStationServiceApi).saveNearbyDevices(Long.valueOf(id), devices);

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.nearbyDevices(id, devices));
        verify(sensorStationServiceApi, times(1)).saveNearbyDevices(Long.valueOf(id), devices);
    }

    @Test
    void getOneSensorStationApi_ExistingSensorStation_ReturnsSensorStation() throws SensorStationNotFoundException {
        SensorStationApi sensorStationApi = new SensorStationApi();
        String expectedResponse = "SensorStationResponse";
        when(sensorStationServiceApi.findOneSensorStation(sensorStationApi.getName())).thenReturn(expectedResponse);

        String result = sensorStationApiController.getOneSensorStationApi(sensorStationApi);

        assertEquals(expectedResponse, result);
        verify(sensorStationServiceApi, times(1)).findOneSensorStation(sensorStationApi.getName());
    }

    @Test
    void getOneSensorStationApi_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        SensorStationApi sensorStationApi = new SensorStationApi();
        when(sensorStationServiceApi.findOneSensorStation(sensorStationApi.getName())).thenThrow(new SensorStationNotFoundException());

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.getOneSensorStationApi(sensorStationApi));
        verify(sensorStationServiceApi, times(1)).findOneSensorStation(sensorStationApi.getName());
    }

    @Test
    void getAllSensorStationApi_ExistingSensorStation_ReturnsSensorStationList() throws SensorStationNotFoundException {
        String id = "1";
        List<String> expectedResponse = Collections.singletonList("SensorStation1");
        when(sensorStationServiceApi.findAllSensorStation(Long.valueOf(id))).thenReturn(expectedResponse);

        List<String> result = sensorStationApiController.getAllSensorStationApi(id);

        assertEquals(expectedResponse, result);
        verify(sensorStationServiceApi, times(1)).findAllSensorStation(Long.valueOf(id));
    }

    @Test
    void getAllSensorStationApi_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        String id = "1";
        when(sensorStationServiceApi.findAllSensorStation(Long.valueOf(id))).thenThrow(new SensorStationNotFoundException());

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.getAllSensorStationApi(id));
        verify(sensorStationServiceApi, times(1)).findAllSensorStation(Long.valueOf(id));
    }

    @Test
    void getSensorStationData_ExistingSensorStation_ReturnsSensorStationDataFrameList() throws SensorStationNotFoundException {
        String id = "1";
        ArrayList<SensorStationDataFrame> expectedResponse = new ArrayList<>();
        when(sensorStationServiceApi.findSensorsByAccesspointID(Long.valueOf(id))).thenReturn(expectedResponse);

        ArrayList<SensorStationDataFrame> result = sensorStationApiController.getSensorStationData(id);

        assertEquals(expectedResponse, result);
        verify(sensorStationServiceApi, times(1)).findSensorsByAccesspointID(Long.valueOf(id));
    }

    @Test
    void getSensorStationData_SensorStationNotFoundException_ThrowsResponseStatusException() throws SensorStationNotFoundException {
        String id = "1";
        when(sensorStationServiceApi.findSensorsByAccesspointID(Long.valueOf(id))).thenThrow(new SensorStationNotFoundException());

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.getSensorStationData(id));
        verify(sensorStationServiceApi, times(1)).findSensorsByAccesspointID(Long.valueOf(id));
    }

    @Test
    void getValidation_ExistingAccessPoint_ReturnsValidationStatus() throws AccessPointNotFoundException {
        String id = "1";
        boolean expectedResponse = true;
        when(sensorStationServiceApi.isValidated(Long.valueOf(id))).thenReturn(expectedResponse);

        boolean result = sensorStationApiController.getValidation(id);

        assertEquals(expectedResponse, result);
        verify(sensorStationServiceApi, times(1)).isValidated(Long.valueOf(id));
    }

    @Test
    void getValidation_AccessPointNotFoundException_ThrowsResponseStatusException() throws AccessPointNotFoundException {
        String id = "1";
        when(sensorStationServiceApi.isValidated(Long.valueOf(id))).thenThrow(new AccessPointNotFoundException());

        assertThrows(ResponseStatusException.class, () -> sensorStationApiController.getValidation(id));
        verify(sensorStationServiceApi, times(1)).isValidated(Long.valueOf(id));
    }
}
