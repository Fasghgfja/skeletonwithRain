package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement2;
import at.qe.skeleton.api.services.MeasurementServiceApi;
import at.qe.skeleton.model.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementControllerTest {

    @Mock
    private MeasurementServiceApi measurementServiceApi;

    @InjectMocks
    private MeasurementController measurementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMeasurement_ValidMeasurement_ReturnsOkStatus() throws MeasurementNotFoundException {
        List<Measurement2> measurementList = Collections.singletonList(new Measurement2());
        doNothing().when(measurementServiceApi).addMeasurement(measurementList);

        int result = measurementController.createMeasurement(measurementList);

        assertEquals(HttpStatus.OK.value(), result);
        verify(measurementServiceApi, times(1)).addMeasurement(measurementList);
    }

    @Test
    void createMeasurement_MeasurementNotFoundException_ThrowsResponseStatusException() throws MeasurementNotFoundException {
        List<Measurement2> measurementList = Collections.singletonList(new Measurement2());
        doThrow(new MeasurementNotFoundException()).when(measurementServiceApi).addMeasurement(measurementList);

        assertThrows(ResponseStatusException.class, () -> measurementController.createMeasurement(measurementList));
        verify(measurementServiceApi, times(1)).addMeasurement(measurementList);
    }

    @Test
    void getOneMeasurement_ExistingMeasurement_ReturnsMeasurement() throws MeasurementNotFoundException {
        Long measurementId = 1L;
        Measurement expectedMeasurement = new Measurement();
        when(measurementServiceApi.findOneMeasurement(measurementId)).thenReturn(expectedMeasurement);

        Measurement result = measurementController.getOneMeasurement(measurementId);

        assertEquals(expectedMeasurement, result);
        verify(measurementServiceApi, times(1)).findOneMeasurement(measurementId);
    }

    @Test
    void getOneMeasurement_MeasurementNotFoundException_ThrowsResponseStatusException() throws MeasurementNotFoundException {
        Long measurementId = 1L;
        when(measurementServiceApi.findOneMeasurement(measurementId)).thenThrow(new MeasurementNotFoundException());

        assertThrows(ResponseStatusException.class, () -> measurementController.getOneMeasurement(measurementId));
        verify(measurementServiceApi, times(1)).findOneMeasurement(measurementId);
    }
}
