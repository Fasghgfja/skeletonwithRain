package at.qe.skeleton.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @Mock
    MeasurementRepository measurementRepository;

    @Mock
    SensorRepository sensorRepository;

    @Mock
    SensorStationRepository sensorStationRepository;

    @InjectMocks
    MeasurementService measurementService;

    SensorStation sensorStation;
    Plant plant;
    Measurement measurement;

    @BeforeEach
    void setup() {
        sensorStation = new SensorStation(); // Add necessary initializations
        plant = new Plant(); // Add necessary initializations
        measurement = new Measurement(); // Add necessary initializations
    }

    @Test
    void getAllMeasurements() {
        measurementService.getAllMeasurements();
        verify(measurementRepository, times(1)).findAll();
    }

    @Test
    void getAllMeasurementsBySensorStation() {
        measurementService.getAllMeasurementsBySensorStation(sensorStation);
        verify(measurementRepository, times(1)).findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation);
    }

    @Test
    void getAllMeasurementsBySensorStationAndTypeAsc() {
        String type = "TEMPERATURE";
        measurementService.getAllMeasurementsBySensorStationAndTypeAsc(sensorStation, type);
        verify(measurementRepository, times(1)).findMeasurementsBySensorStationAndTypeLikeOrderByTimestampAsc(sensorStation, type);
    }

    @Test
    void getAllMeasurementsBySensorStationAndType() {
        String type = "TEMPERATURE";
        measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, type);
        verify(measurementRepository, times(1)).findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type);
    }

    @Test
    void getLatestPlantMeasurements() {
        measurementService.getLatestPlantMeasurements(sensorStation);
        verify(measurementRepository, times(6)).findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(eq(sensorStation), anyString());
    }

    @Test
    void getLatestPlantMeasurementsWithPlant() {
        measurementService.getLatestPlantMeasurements(plant);
        verify(measurementRepository, times(6)).findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(eq(plant), anyString());
    }

    @Test
    void getMeasurementsAmount() {
        when(measurementRepository.count()).thenReturn(10);
        assertEquals(10, measurementService.getMeasurementsAmount());
    }

    @Test
    void getMeasurementTypeIcon() {
        String type = "HUMIDITY";
        String expected = "fa-solid fa-droplet fa-lg";
        assertEquals(expected, measurementService.getMeasurementTypeIcon(type));
    }

    @Test
    void getLastMeasurementBySensorStationAndType() {
        String type = "TEMPERATURE";
        measurementService.getLastMeasurementBySensorStationAndType(sensorStation, type);
        verify(measurementRepository, times(1)).getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation, type);
    }

    @Test
    void doGetMeasurementsByTypeAndSensorStationAndTimestampBetween() {
        String chosenMeasurement = "TEMPERATURE";
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime dateTo = LocalDateTime.now();
        measurementService.doGetMeasurementsByTypeAndSensorStationAndTimestampBetween(chosenMeasurement, sensorStation, dateFrom, dateTo);
        verify(measurementRepository, times(1)).getMeasurementsByTypeAndSensorStationAndTimestampBetweenOrderByTimestampAsc(chosenMeasurement, sensorStation, dateFrom, dateTo);
    }

    @Test
    void doFindFirstBySensorStationOrderByTimestampAsc() {
        measurementService.doFindFirstBySensorStationOrderByTimestampAsc(sensorStation);
        verify(measurementRepository, times(1)).findFirstBySensorStationOrderByTimestampAsc(sensorStation);
    }

    @Test
    void setMeasurementRepository() {
        MeasurementRepository measurementRepository = mock(MeasurementRepository.class);
        measurementService.setMeasurementRepository(measurementRepository);
        assertEquals(measurementRepository, measurementService.getMeasurementRepository());
    }

    @Test
    void setSensorStationRepository() {
        SensorStationRepository sensorStationRepository = mock(SensorStationRepository.class);
        measurementService.setSensorStationRepository(sensorStationRepository);
        assertEquals(sensorStationRepository, measurementService.getSensorStationRepository());
    }

    @Test
    void getAllMeasurements_emptyRepository() {
        when(measurementRepository.findAll()).thenReturn(Collections.emptyList());
        Collection<Measurement> measurements = measurementService.getAllMeasurements();
        assertTrue(measurements.isEmpty());
    }

    @Test
    void getAllMeasurementsBySensorStation_emptyRepository() {
        when(measurementRepository.findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation))
                .thenReturn(Collections.emptyList());
        Collection<Measurement> measurements = measurementService.getAllMeasurementsBySensorStation(sensorStation);
        assertTrue(measurements.isEmpty());
    }

    @Test
    void getAllMeasurementsBySensorStationAndTypeAsc_emptyRepository() {
        String type = "TEMPERATURE";
        when(measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampAsc(sensorStation, type))
                .thenReturn(Collections.emptyList());
        Collection<Measurement> measurements = measurementService.getAllMeasurementsBySensorStationAndTypeAsc(sensorStation, type);
        assertTrue(measurements.isEmpty());
    }

    @Test
    void getAllMeasurementsBySensorStationAndType_emptyRepository() {
        String type = "TEMPERATURE";
        when(measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type))
                .thenReturn(Collections.emptyList());
        Collection<Measurement> measurements = measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, type);
        assertTrue(measurements.isEmpty());
    }

    @Test
    void getLatestPlantMeasurements_emptyRepository() {
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.HUMIDITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.TEMPERATURE.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.SOIL_MOISTURE.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.LIGHT_INTENSITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.AIR_QUALITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.AIR_PRESSURE.getValue()))
                .thenReturn(null);

        Collection<Measurement> measurements = measurementService.getLatestPlantMeasurements(sensorStation);
        assertFalse(measurements.isEmpty());
    }

    @Test
    void getLatestPlantMeasurementsWithPlant_emptyRepository() {
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.HUMIDITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.TEMPERATURE.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.SOIL_MOISTURE.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.LIGHT_INTENSITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.AIR_QUALITY.getValue()))
                .thenReturn(null);
        when(measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.AIR_PRESSURE.getValue()))
                .thenReturn(null);

        Collection<Measurement> measurements = measurementService.getLatestPlantMeasurements(plant);
        assertFalse(measurements.isEmpty());
    }

    @Test
    void getLastMeasurementBySensorStationAndType_noMeasurementFound() {
        String type = "TEMPERATURE";
        when(measurementRepository.getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation, type))
                .thenReturn(null);

        String value = measurementService.getLastMeasurementBySensorStationAndType(sensorStation, type);
        assertEquals("--", value);
    }


    @Test
    void getCurrentUser_authenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        String currentUser = measurementService.getCurrentUser();
        assertEquals("testUser", currentUser);
    }

    @Test
    void getCurrentUser_unauthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String currentUser = measurementService.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    void testDoGetMeasurementsByTypeAndSensorStationAndTimestampBetween_emptyRepository() {
        String type = "TEMPERATURE";
        LocalDateTime dateFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime dateTo = LocalDateTime.now();
        when(measurementRepository.getMeasurementsByTypeAndSensorStationAndTimestampBetweenOrderByTimestampAsc(type, sensorStation, dateFrom, dateTo))
                .thenReturn(Collections.emptyList());
        Collection<Measurement> measurements = measurementService.doGetMeasurementsByTypeAndSensorStationAndTimestampBetween(type, sensorStation, dateFrom, dateTo);
        assertTrue(measurements.isEmpty());
    }

    @Test
    void testDoFindFirstBySensorStationOrderByTimestampAsc_noMeasurementFound() {
        when(measurementRepository.findFirstBySensorStationOrderByTimestampAsc(sensorStation))
                .thenReturn(null);
        Measurement measurement = measurementService.doFindFirstBySensorStationOrderByTimestampAsc(sensorStation);
        assertNull(measurement);
    }

    @Test
    void testGetCurrentUser_nullAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
        String currentUser = measurementService.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    void getMeasurementsAmount_zeroMeasurements() {
        when(measurementRepository.count()).thenReturn(0);
        assertEquals(0, measurementService.getMeasurementsAmount());
    }

    @Test
    void getMeasurementTypeIcon_unknownType() {
        String type = "UNKNOWN";
        assertEquals("", measurementService.getMeasurementTypeIcon(type));
    }

    @Test
    void deleteMeasurementsFromTo_nullFromAndTo() {
        measurementService.deleteMeasurementsFromTo(null, null);
        verify(measurementRepository, never()).deleteMeasurementsByTimestampBetween(any(), any());
    }

    @Test
    void deleteMeasurementsFromTo_fromAfterTo() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.minusDays(1);
        measurementService.deleteMeasurementsFromTo(from, to);
        verify(measurementRepository, never()).deleteMeasurementsByTimestampBetween(any(), any());
    }

    @Test
    void deleteMeasurementsFromTo_emptyMeasurementRepository() {
        when(measurementRepository.findFirstByOrderByTimestampAsc()).thenReturn(null);
        measurementService.deleteMeasurementsFromTo(null, null);
        verify(measurementRepository, never()).deleteMeasurementsByTimestampBetween(any(), any());
    }

    @Test
    void deleteMeasurementsFromToForSensorStation_nullFromAndTo() {
        String sensorStationToDeleteFromId = "sensorStationId";
        measurementService.deleteMeasurementsFromToForSensorStation(null, null, sensorStationToDeleteFromId);
        verify(measurementRepository, never()).deleteMeasurementsBySensorStationAndTimestampBetween(any(), any(), any());
    }

    @Test
    void deleteMeasurementsFromToForSensorStation_fromAfterTo() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.minusDays(1);
        String sensorStationToDeleteFromId = "sensorStationId";
        measurementService.deleteMeasurementsFromToForSensorStation(from, to, sensorStationToDeleteFromId);
        verify(measurementRepository, never()).deleteMeasurementsBySensorStationAndTimestampBetween(any(), any(), any());
    }
}
