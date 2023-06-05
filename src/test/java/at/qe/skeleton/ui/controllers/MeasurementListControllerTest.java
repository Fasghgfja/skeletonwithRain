package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementType;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Import(MeasurementListController.class)
class MeasurementListControllerTest {

    private MeasurementListController measurementListController;

    @Mock
    private MeasurementService measurementService;

    @Mock
    private SensorService sensorService;

    @Mock
    private SensorStationService sensorStationService;

    @Mock
    private SensorStationDetailController sensorStationDetailController;

    @Mock
    private GraphController graphController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        measurementListController = new MeasurementListController();
        measurementListController.setMeasurementService(measurementService);
        measurementListController.setSensorService(sensorService);
        measurementListController.setSensorStationService(sensorStationService);
        measurementListController.setSensorStationDetailController(sensorStationDetailController);
        measurementListController.setGraphController(graphController);
    }

    @Test
    void testDeleteFromToForSensorStation_WithNullSensorStationToDeleteFromId() {
        measurementListController.setSensorStationToDeleteFromId(null);

        measurementListController.deleteFromToForSensorStation();

        verify(measurementService, never()).deleteMeasurementsFromToForSensorStation(any(), any(), any());
    }

    @Test
    void testDeleteFromToForSensorStation_WithNonNullSensorStationToDeleteFromId() {
        measurementListController.setSensorStationToDeleteFromId("sensorStationId");
        ScheduleEvent event = new DefaultScheduleEvent();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        measurementListController.setEvent(event);

        measurementListController.deleteFromToForSensorStation();

        verify(measurementService, times(1)).deleteMeasurementsFromToForSensorStation(startDate, endDate, "sensorStationId");
    }

    @Test
    void testDeleteFromTo_WithNullEventStartDateAndEndDate() {
        ScheduleEvent event = new DefaultScheduleEvent();
        measurementListController.setEvent(event);

        measurementListController.deleteFromTo();

        verify(measurementService, never()).deleteMeasurementsFromTo(any(), any());
    }

    @Test
    void testDeleteFromTo_WithNonNullEventStartDateAndEndDate() {
        ScheduleEvent event = new DefaultScheduleEvent();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        measurementListController.setEvent(event);

        measurementListController.deleteFromTo();

        verify(measurementService, times(1)).deleteMeasurementsFromTo(startDate, endDate);
    }

    @Test
    void testInit_WithNullSensorStation() {
        measurementListController.setSensorStationDetailController(sensorStationDetailController);
        measurementListController.setGraphController(graphController);
        List<Measurement> expectedMeasurements = new ArrayList<>();
        when(measurementService.getAllMeasurements()).thenReturn(expectedMeasurements);

        measurementListController.init();

        assertEquals(expectedMeasurements, measurementListController.getMeasurements());
        verify(measurementService, times(1)).getAllMeasurements();
    }

    @Test
    void testInit_WithNonNullSensorStationAndAllType() {
        SensorStation sensorStation = new SensorStation();
        measurementListController.setSensorStationDetailController(sensorStationDetailController);
        measurementListController.setGraphController(graphController);
        measurementListController.setType("all");
        measurementListController.setMeasurementsForSensorStationList(null);
        when(sensorStationDetailController.getSensorStation()).thenReturn(sensorStation);
        List<Measurement> expectedMeasurements = new ArrayList<>();
        when(measurementService.getAllMeasurementsBySensorStation(sensorStation)).thenReturn(expectedMeasurements);

        measurementListController.init();

        assertEquals(expectedMeasurements, measurementListController.getMeasurementsForSensorStationList());
        verify(sensorStationDetailController, times(1)).getSensorStation();
        verify(measurementService, times(1)).getAllMeasurementsBySensorStation(sensorStation);
    }

    @Test
    void testInit_WithNonNullSensorStationAndSpecificType() {
        SensorStation sensorStation = new SensorStation();
        measurementListController.setSensorStationDetailController(sensorStationDetailController);
        measurementListController.setGraphController(graphController);
        measurementListController.setType(MeasurementType.AIR_QUALITY.getValue());
        measurementListController.setMeasurementsForSensorStationList(null);
        when(sensorStationDetailController.getSensorStation()).thenReturn(sensorStation);
        List<Measurement> expectedMeasurements = new ArrayList<>();
        when(measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, MeasurementType.AIR_QUALITY.getValue())).thenReturn(expectedMeasurements);

        measurementListController.init();

        assertEquals(expectedMeasurements, measurementListController.getMeasurementsForSensorStationList());
        verify(sensorStationDetailController, times(1)).getSensorStation();
        verify(measurementService, times(1)).getAllMeasurementsBySensorStationAndType(sensorStation, MeasurementType.AIR_QUALITY.getValue());
    }

    // Add tests for the remaining methods...
}
