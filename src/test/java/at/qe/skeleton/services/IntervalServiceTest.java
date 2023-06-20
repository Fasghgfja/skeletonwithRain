package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SSInterval;
import at.qe.skeleton.repositories.IntervalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.prepost.PreAuthorize;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IntervalServiceTest {

    @Mock
    private IntervalRepository intervalRepository;

    @InjectMocks
    private IntervalService intervalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFirstById_ValidId_ReturnsSSInterval() {
        // Arrange
        SSInterval expectedInterval = new SSInterval();
        when(intervalRepository.findFirstById(anyLong())).thenReturn(expectedInterval);

        // Act
        SSInterval result = intervalService.getFirstById(1L);

        // Assert
        verify(intervalRepository, times(1)).findFirstById(eq(1L));
        assertEquals(expectedInterval, result);
    }

    @Test
    void getFirstBySensorStationId_ValidSensorStationId_ReturnsSSInterval() {
        // Arrange
        SSInterval expectedInterval = new SSInterval();
        when(intervalRepository.findFirstBySensorStationId(anyString())).thenReturn(expectedInterval);

        // Act
        SSInterval result = intervalService.getFirstBySensorStationId("sensorStationId");

        // Assert
        verify(intervalRepository, times(1)).findFirstBySensorStationId(eq("sensorStationId"));
        assertEquals(expectedInterval, result);
    }

    @Test
    void getFirstBySensorStation_ValidSensorStation_ReturnsSSInterval() {
        // Arrange
        SSInterval expectedInterval = new SSInterval();
        SensorStation sensorStation = new SensorStation();
        when(intervalRepository.findFirstBySensorStation(any(SensorStation.class))).thenReturn(expectedInterval);

        // Act
        SSInterval result = intervalService.getFirstBySensorStation(sensorStation);

        // Assert
        verify(intervalRepository, times(1)).findFirstBySensorStation(eq(sensorStation));
        assertEquals(expectedInterval, result);
    }

    @Test
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    void saveInterval_ValidInterval_ReturnsSavedSSInterval() {
        // Arrange
        SSInterval interval = new SSInterval();
        when(intervalRepository.save(any(SSInterval.class))).thenReturn(interval);

        // Act
        SSInterval result = intervalService.saveInterval(interval);

        // Assert
        verify(intervalRepository, times(1)).save(eq(interval));
        assertEquals(interval, result);
    }

    @Test
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    void deleteInterval_ValidInterval_DeletesInterval() {
        // Arrange
        SSInterval interval = new SSInterval();

        // Act
        assertDoesNotThrow(() -> intervalService.deleteInterval(interval));

        // Assert
        verify(intervalRepository, times(1)).delete(eq(interval));
    }

    @Test
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    void deleteIntervalBySensorStation_ValidSensorStation_DeletesInterval() {
        // Arrange
        SensorStation sensorStation = new SensorStation();

        // Act
        assertDoesNotThrow(() -> intervalService.deleteIntervalBySensorStation(sensorStation));

        // Assert
        verify(intervalRepository, times(1)).deleteIntervalBySensorStation(eq(sensorStation));
    }

    @Test
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    void deleteIntervalBySensorStationId_ValidSensorStationId_DeletesInterval() {
        // Arrange
        String sensorStationId = "sensorStationId";

        // Act
        assertDoesNotThrow(() -> intervalService.deleteIntervalBySensorStationId(sensorStationId));

        // Assert
        verify(intervalRepository, times(1)).deleteIntervalBySensorStationId(eq(sensorStationId));
    }
}
