package at.qe.skeleton.ui.controllers;
import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ToggleEvent;
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
    private SensorStationService sensorStationService;
    @Autowired
    private transient GalleryController galleryController;
    @Autowired
    private SessionInfoBean sessionInfoBean;

    private Collection<Plant> filteredPlants;

    /**
     * Cached plant.
     */
    private Plant plant;
    private Collection<Measurement> latestMeasurements;
    private String plantName = "";

    private String plantId;
    private String description = "";


    private String selectedPlantName;

    /**
     * Returns a list of all plants.
     */
    public Collection<Plant> getPlants() {
        return plantService.getAllPlants();
    }


    /**
     * The method is Only used in the scrolldown menu for plant selection.
     */
    public Collection<String> getPlantsUniqueNames() {
        return plantService.getAllPlantsUniqueNames();
    }

    /**
     * Returns how many plants are registered in the system.
     */
    public Long getPlantsAmount() {
        return plantService.getPlantsAmount();
    }

    public Collection<Plant> doGetOnlyPlantsNotYetFollowed() {
        Userx user = sessionInfoBean.getCurrentUser();
        return this.plantService.getOnlyPlantsNotYetFollowed(user);

    }

    public Collection<Plant> doGetFollowedPlants() {
        Userx user = sessionInfoBean.getCurrentUser();
        return this.plantService.getFollowedPlants(user);

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
        else System.out.println("cannot delete a plant that is currently displayed in a sensor station , im plant controller");
    }




    public String getMeasurementStatus(String measurementId,String type) {//TODO:marco look here (fix in case there are no measurements, aka when measurementId is ""
        Measurement thisMeasurement = measurementService.findMeasurementById(Long.parseLong(measurementId));
        if (thisMeasurement == null) {return "OK";}
        if (checkThreshold(thisMeasurement,type) == 0){return "OK";} else {return "Wrong";}
    }


    public String getlastMeasurementStatus(String type,String sensorStationId) {
        Measurement thisMeasurement = measurementService.findFirstMeasurementBySensorStationIdAndType(sensorStationId,type);
        if (thisMeasurement == null) {return "OK";}
        if (checkThreshold(thisMeasurement,type) == 0){return "OK";} else {return "Wrong";}
    }

    public String getlastMeasurementStatusIcon(String type) {
        switch(type) {
            case "HUMIDITY":
                return "fa-solid fa-droplet fa-lg";
            case "TEMPERATURE":
                return "fa-solid fa-thermometer-three-quarters fa-lg";
            case "AIR_PRESSURE":
                return "fa-sharp fa-solid fa-arrows-to-circle fa-lg";
            case "LIGHT_INTENSITY":
                return "fa-solid fa-sun fa-lg";
            case "SOIL_MOISTURE":
                return "fa-solid fa-water fa-lg";
            case "AIR_QUALITY":
                return "fa-solid fa-wind fa-lg";
            default:
                return "";
        }
    }

    private int checkThreshold(Measurement measurement, String type) {
        boolean isThresholdExceeded;
        switch(type) {
            case "SOIL_MOISTURE":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) > 95 || Double.parseDouble(measurement.getValue_s()) < 10);
                return isThresholdExceeded ? 1 : 0;
            case "HUMIDITY":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) > 80 || Double.parseDouble(measurement.getValue_s()) < 20);
                return isThresholdExceeded ? 1 : 0;
            case "AIR_PRESSURE":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) > 2 || Double.parseDouble(measurement.getValue_s()) < 1);
                return isThresholdExceeded ? 1 : 0;
            case "TEMPERATURE":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) > 35 || Double.parseDouble(measurement.getValue_s()) < 10);
                return isThresholdExceeded ? 1 : 0;
            case "AIR_QUALITY":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) < 50);
                return isThresholdExceeded ? 1 : 0;
            case "LIGHT_INTENSITY":
                isThresholdExceeded = (Double.parseDouble(measurement.getValue_s()) > 1500 || Double.parseDouble(measurement.getValue_s()) < 100);
                return isThresholdExceeded ? 1 : 0;
            default:
                return 0;
        }
    }





    /**
     * The method gets the last mesurements for a given sensor station.
     * the method fetches the latest measurements of the cached Sensor station
     * NOTE: this method is called from  {@link SensorStationDetailController#onRowToggle(ToggleEvent)}
     * and {@link SensorStationDetailController#onRowSelectLineChart(ToggleEvent)}.
     * When the sensor station row is toggled and the cached sensor station updated.
     *
     * @return the latest measurements for the cached sensor station (1 per type).
     */
    public Collection<Measurement> getLatestMeasurements() {
        latestMeasurements = measurementService.getLatestPlantMeasurements(plant);
        return latestMeasurements;
    }





    /**
     * Methods to get the images of the plant currently displayed in the sensor station.
     */
    public List<Image> doGetPlantImages() {
        if (plantId == null){return new ArrayList<Image>();}
        return galleryController.doGetPlantImages(plantId);
    }

    public List<Image> doGetApprovedPlantImages() {//TODO fix the error handling like in sensor station detail controller the method with same name
        //if (idString == null){return new ArrayList<Image>();}
        //return imageService.getAllPlantImages(idString);
        return galleryController.doGetApprovedPlantImages(plantId);
    }

    //TODO: Remove along the hierarchy and replace with something more elegant
    public void setPlantFromId(Long id) {
        this.plant = plantService.loadPlant(id);
    }




    //TODO: simplify this monstruosity

    /**
     * Method to initialize a greenhouse/sensor station view for a specific greenhouse taken from facescontext.
     */
    public void init() {
        if (this.plant != null) return;
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        Long id = Long.parseLong(params.get("id"));
        this.setPlantFromId(id);


    }


    public Boolean getIsPlantAlreadyFollowed(Plant plant) {
        return !plantService.isPlantAlreadyFollowed(sessionInfoBean.getCurrentUser(), plant);
    }
}
