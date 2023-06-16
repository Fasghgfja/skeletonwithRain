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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MeasurementServiceApiTest {

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
    void testFindOneMeasurement_NotFoundException() {
        // Arrange
        Long id = 1L;

        // Mocking the repository behavior
        when(measurementRepository.findById(eq(id)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MeasurementNotFoundException.class, () -> measurementServiceApi.findOneMeasurement(id));
    }


}
