package at.qe.skeleton.ui.controllers;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
public class PlantController implements Serializable{
    @Autowired
    private PlantService plantService;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private transient GalleryController galleryController;
    @Autowired
    private SessionInfoBean sessionInfoBean;

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
    public void initList(){//TODO: move this in the plantlist controller
        plantList = (ArrayList<Plant>) plantService.getAllPlants();
        followedPlantsList = (ArrayList<Plant>) plantService.getFollowedPlants(sessionInfoBean.getCurrentUser());
    }


    //TODO: is it quicker if we look by id or by plant?
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
    //TODO: implement stronger error handling
    public void doDeletePlant() {
        if (plant.getSensorStation() == null) {
            System.out.println("No sensor station assigned im deleting the plant");
            plantService.detachAllImagesFromPlant(plant);
            this.plantService.deletePlant(plant);
            plant = null;
        }
        else plantService.deletePlantWithStation(plant);
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
     *
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
