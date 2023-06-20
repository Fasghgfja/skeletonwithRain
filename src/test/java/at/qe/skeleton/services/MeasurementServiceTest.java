package at.qe.skeleton.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void setSensorRepository() {
        SensorRepository sensorRepository = mock(SensorRepository.class);
        measurementService.setSensorRepository(sensorRepository);
        // Verify that the method does not throw any exceptions
    }

    @Test
    void setSensorStationRepository() {
        SensorStationRepository sensorStationRepository = mock(SensorStationRepository.class);
        measurementService.setSensorStationRepository(sensorStationRepository);
        assertEquals(sensorStationRepository, measurementService.getSensorStationRepository());
    }

}
