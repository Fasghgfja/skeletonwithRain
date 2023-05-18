package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope("view")
public class PlantController implements Serializable {

    private static final String PLANT_DELETED = "PLANT DELETED: ";
    @Autowired
    private transient PlantService plantService;
    @Autowired
    private transient MeasurementService measurementService;
    @Autowired
    private transient GalleryController galleryController;
    @Autowired
    private SessionInfoBean sessionInfoBean;
    @Autowired
    private transient LogRepository logRepository;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;

    private Collection<Plant> filteredPlants;

    private List<Plant> plantList;
    private List<Plant> followedPlantsList;

    /**
     * Cached plant.
     */
    private Plant plant;
    private Collection<Measurement> latestMeasurements;
    private String plantName = "";

    private String plantId;
    private String description = "";


    private String selectedPlantName;


    @PostConstruct
    public void initList() {
        plantList = (ArrayList<Plant>) plantService.getAllPlants();
        followedPlantsList = (ArrayList<Plant>) plantService.getFollowedPlants(sessionInfoBean.getCurrentUser());
    }


    public void setPlantFromId(Long id) {
        this.plant = plantService.loadPlant(id);
    }


    public Boolean getIsPlantAlreadyFollowed(Plant plant) {
        return !plantService.isPlantAlreadyFollowed(sessionInfoBean.getCurrentUser(), plant);
    }


    /**
     * Action to save the currently cached Plant.
     */
    public void doSavePlant() {
        plant = this.plantService.savePlant(plant);
    }

    /**
     * Action to delete the currently cached Sensor Station.
     */
    public void doDeletePlant() {
        if (plant.getSensorStation() == null) {
            plantService.detachAllImagesFromPlant(plant);
            this.plantService.deletePlant(plant);
            try {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
                successLogger.info(PLANT_DELETED + plant.getId());
                successFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log createLog = new Log();
            createLog.setDate(LocalDate.now());
            createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            createLog.setAuthor(sessionInfoBean.getCurrentUserName());
            createLog.setSubject("PLANT DELETION");
            createLog.setText(PLANT_DELETED + plant.getId());
            createLog.setType(LogType.SUCCESS);
            logRepository.save(createLog);
            plant = null;
        } else {
            plantService.deletePlantWithStation(plant);
            try {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
                successLogger.info(PLANT_DELETED + plant.getId());
                successFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log createLog = new Log();
            createLog.setDate(LocalDate.now());
            createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            createLog.setAuthor(sessionInfoBean.getCurrentUserName());
            createLog.setSubject("PLANT DELETION");
            createLog.setText(PLANT_DELETED + plant.getId());
            createLog.setType(LogType.SUCCESS);
            logRepository.save(createLog);
        }
    }


    /**
     * The method gets the last mesurements for a given sensor station.
     * the method fetches the latest measurements of the cached Sensor station
     * When the sensor station row is toggled and the cached sensor station updated.
     *
     * @return the latest measurements for the cached sensor station (1 per type).
     */
    public Collection<Measurement> getLatestMeasurements() {
        latestMeasurements = measurementService.getLatestPlantMeasurements(plant);
        return latestMeasurements;
    }


    /**
     * Method to initialize a greenhouse/sensor station view for a specific greenhouse taken from facescontext.
     * <p>
     * Who calls this: PlantPage,
     */
    public void init() {
        if (this.plant != null) return;
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        Long id = Long.parseLong(params.get("id"));
        this.setPlantFromId(id);
    }

}
