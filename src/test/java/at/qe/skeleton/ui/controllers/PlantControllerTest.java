package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlantControllerTest {

    private PlantController plantController;

    @Mock
    private PlantService plantService;
    @Mock
    private MeasurementService measurementService;
    @Mock
    private GalleryController galleryController;
    @Mock
    private SessionInfoBean sessionInfoBean;
    @Mock
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        plantController = new PlantController();
        plantController.setPlantService(plantService);
        plantController.setMeasurementService(measurementService);
        plantController.setGalleryController(galleryController);
        plantController.setSessionInfoBean(sessionInfoBean);
        plantController.setLogRepository(logRepository);
    }

    @Test
    void testInitList() {
        List<Plant> plantList = new ArrayList<>();
        List<Plant> followedPlantsList = new ArrayList<>();

        when(plantService.getAllPlants()).thenReturn(plantList);
        when(plantService.getFollowedPlants(any())).thenReturn(followedPlantsList);

        plantController.initList();

        verify(plantService, times(1)).getAllPlants();
        verify(plantService, times(1)).getFollowedPlants(any());
        assertEquals(plantList, plantController.getPlantList());
        assertEquals(followedPlantsList, plantController.getFollowedPlantsList());
    }

    @Test
    void testSetPlantFromId() {
        Plant plant = new Plant();
        plant.setId(1L);

        when(plantService.loadPlant(1L)).thenReturn(plant);

        plantController.setPlantFromId(1L);

        verify(plantService, times(1)).loadPlant(1L);
        assertEquals(plant, plantController.getPlant());
    }

    @Test
    void testGetIsPlantAlreadyFollowed_WhenPlantIsFollowed() {
        Plant plant = new Plant();

        when(plantService.isPlantAlreadyFollowed(any(), any())).thenReturn(true);

        boolean result = plantController.getIsPlantAlreadyFollowed(plant);

        verify(plantService, times(1)).isPlantAlreadyFollowed(any(), any());
        assertFalse(result);
    }

    @Test
    void testGetIsPlantAlreadyFollowed_WhenPlantIsNotFollowed() {
        Plant plant = new Plant();

        when(plantService.isPlantAlreadyFollowed(any(), any())).thenReturn(false);

        boolean result = plantController.getIsPlantAlreadyFollowed(plant);

        verify(plantService, times(1)).isPlantAlreadyFollowed(any(), any());
        assertTrue(result);
    }

    @Test
    void testDoSavePlant() {
        Plant plant = new Plant();

        when(plantService.savePlant(plant)).thenReturn(plant);

        plantController.setPlant(plant);
        plantController.doSavePlant();

        verify(plantService, times(1)).savePlant(plant);
        assertEquals(plant, plantController.getPlant());
    }

    @Test
    void testDoDeletePlant_WithoutSensorStation() throws IOException {
        Plant plant = new Plant();
        plant.setId(1L);

        when(plant.getSensorStation()).thenReturn(null);
        when(logRepository.save(any(Log.class))).thenReturn(null);

        plantController.setPlant(plant);

        plantController.doDeletePlant();

        verify(plantService, times(1)).detachAllImagesFromPlant(plant);
        verify(plantService, times(1)).deletePlant(plant);
        verify(logRepository, times(1)).save(any(Log.class));

        assertNull(plantController.getPlant());
    }

    @Test
    void testDoDeletePlant_WithSensorStation() throws IOException {
        Plant plant = new Plant();
        plant.setId(1L);

        when(plant.getSensorStation()).thenReturn(new SensorStation());
        when(logRepository.save(any(Log.class))).thenReturn(null);

        plantController.setPlant(plant);

        plantController.doDeletePlant();

        verify(plantService, times(1)).deletePlantWithStation(plant);
        verify(logRepository, times(1)).save(any(Log.class));

        assertNull(plantController.getPlant());
    }

    @Test
    void testGetLatestMeasurements() {
        Plant plant = new Plant();
        Collection<Measurement> measurements = new ArrayList<>();

        when(measurementService.getLatestPlantMeasurements(plant)).thenReturn(measurements);

        plantController.setPlant(plant);

        Collection<Measurement> result = plantController.getLatestMeasurements();

        verify(measurementService, times(1)).getLatestPlantMeasurements(plant);
        assertEquals(measurements, result);
        assertEquals(measurements, plantController.getLatestMeasurements());
    }

}
