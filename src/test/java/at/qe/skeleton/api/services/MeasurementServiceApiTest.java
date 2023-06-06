package at.qe.skeleton.api.services;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.Measurement2;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.services.SensorStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MeasurementServiceApiTest {
/*
    private MeasurementServiceApi measurementServiceApi;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorStationService sensorStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        measurementServiceApi = new MeasurementServiceApi();
        measurementServiceApi.measurementRepository = measurementRepository;
        measurementServiceApi.sensorStationService = sensorStationService;
    }

    @Test
    void testAddMeasurement_Success() throws MeasurementNotFoundException {
        // Arrange
        Measurement2 measurement2 = new Measurement2();
        measurement2.setTime_stamp("2023-06-05 12:00:00");
        measurement2.setSensorStation("station");
        measurement2.setType("type");
        measurement2.setValue("value");

        SensorStation sensorStation = new SensorStation();
        when(sensorStationService.loadSensorStation(eq(measurement2.getSensorStation())))
                .thenReturn(sensorStation);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(measurement2.getTime_stamp(), formatter);

        Measurement measurement = new Measurement();
        measurement.setSensorStation(sensorStation);
        measurement.setType(measurement2.getType());
        measurement.setTimestamp(dateTime);
        measurement.setValue_s(measurement2.getValue());

        // Mocking the repository behavior
        when(measurementRepository.save(any(Measurement.class)))
                .thenReturn(measurement);

        // Act
        measurementServiceApi.addMeasurement(measurement2);

        // Assert
        verify(sensorStationService, times(1)).loadSensorStation(eq(measurement2.getSensorStation()));
        verify(measurementRepository, times(1)).save(any(Measurement.class));
    }
    //es wird nun eine liste übergeben
    @Test
    void testAddMeasurement_NotFoundException() throws MeasurementNotFoundException {
        // Arrange
        Measurement2 measurement2 = new Measurement2();
        measurement2.setTime_stamp("2023-06-05 12:00:00");
        measurement2.setSensorStation("station");
        measurement2.setType("type");
        measurement2.setValue("value");

        when(sensorStationService.loadSensorStation(eq(measurement2.getSensorStation())))
                .thenThrow(new SensorStationNotFoundException());

        // Act & Assert
        assertThrows(MeasurementNotFoundException.class, () -> measurementServiceApi.addMeasurement(measurement2));
    }

    @Test
    void testFindOneMeasurement_Success() throws MeasurementNotFoundException {
        // Arrange
        Long id = 1L;
        Measurement measurement = new Measurement();
        measurement.setId(id);

        // Mocking the repository behavior
        when(measurementRepository.findById(eq(id)))
                .thenReturn(Optional.of(measurement));

        // Act
        Measurement response = measurementServiceApi.findOneMeasurement(id);

        // Assert
        verify(measurementRepository, times(1)).findById(eq(id));
        assertEquals(response, measurement);
    }

    @Test
    void testFindOneMeasurement_NotFoundException() {
        // Arrange
        Long id = 1L;

        // Mocking the repository behavior
        when(measurementRepository.findById(eq(id)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MeasurementNotFoundException.class, () -> measurementServiceApi.findOneMeasurement(id));
    }

    @Test
    void testUpdateMeasurement_Success() throws MeasurementNotFoundException {
        // Arrange
        Long id = 1L;
        Measurement measurement = new Measurement();
        measurement.setId(id);
        measurement.setValue_s("new value");

        // Mocking the repository behavior
        when(measurementRepository.findById(eq(id)))
                .thenReturn(Optional.of(measurement));
        when(measurementRepository.save(any(Measurement.class)))
                .thenReturn(measurement);

        // Act
        Measurement response = measurementServiceApi.updateMeasurement(id, measurement);

        // Assert
        verify(measurementRepository, times(1)).findById(eq(id));
        verify(measurementRepository, times(1)).save(any(Measurement.class));
        assertEquals(response, measurement);
    }

    @Test
    void testUpdateMeasurement_NotFoundException() {
        // Arrange
        Long id = 1L;
        Measurement measurement = new Measurement();
        measurement.setId(id);
        measurement.setValue_s("new value");

        // Mocking the repository behavior
        when(measurementRepository.findById(eq(id)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MeasurementNotFoundException.class, () -> measurementServiceApi.updateMeasurement(id, measurement));
    }

 */
}
