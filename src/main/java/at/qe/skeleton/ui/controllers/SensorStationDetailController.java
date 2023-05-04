package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.*;

import java.io.Serializable;
import java.util.*;

import at.qe.skeleton.repositories.AbstractRepository;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Controller for the sensor stations detail view.
 */
@Getter
@Setter
@Component
@Scope("view")
public class SensorStationDetailController implements Serializable {


    /**
     * Autowired dependencies.
     * Spring will automatically resolve and inject a matching bean from the Spring application context at runtime.
     */
    @Autowired
    private SensorStationService sensorService;
    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private PlantService plantService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    @Autowired
    private transient GraphController graphController;

//todo:pray this isnt too much
    @Autowired
    private transient GalleryController galleryController;



    /**
     * Tells us if the sensor station is new , replace with a more elegant solution!.
     */
    private boolean newSensorStation;

    /**
     * Attribute to cache the currently displayed sensor station.
     */
    private SensorStation sensorStation;

    /**
     * Attribute to cache the latestMEasurements.
     */
    private Collection<Measurement> latestMeasurements;

    /**
     * maybe not needed fields.
     */
    private String plantName = "";

    private String plantId;
    private String description = "";
    private Plant plant;

    private String selectedPlantName;

    public String getSelectedPlantName() {
        return selectedPlantName;
    }

    public void setSelectedPlantName(String selectedPlantName) {
        this.selectedPlantName = selectedPlantName;
    }

    boolean fixed = false;




    //TODO: simplify this to user javax unselect event to just remove the gardenrs with only one checkbox menu
    List<String> gardeners;
    List<Userx> gardenersToRemove;

    public void doAddGardenerToSensorStation(String user) {//TODO:use this
        this.sensorService.addGardenerToSensorStation(sensorStation,user);
    }
    public void doAddGardenersToSensorStation() {//TODO:use this
        gardeners.forEach(this::doAddGardenerToSensorStation);
    }
    public void doRemoveGardenerFromSensorStation(Userx user) {//TODO:use this
        this.sensorService.removeGardenerFromSensorStation(sensorStation,user);
    }
    public void doRemoveGardenersFromSensorStation() {//TODO:use this
        gardenersToRemove.forEach(this::doRemoveGardenerFromSensorStation);
    }






        public String getMeasurementStatus(String measurementId,String type) {//TODO:marco look here : D
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
     * Opens last measurements row toggle for selected sensor station
     * it updates the cached sensor station with the one that is toggled in the UI table.
     * The method is used in the Greenhouses.chtml aka manage greenhouses page to open the info
     * about the last measurements (1 per type) .
     * the method calls {@link SensorStationDetailController#getLatestMeasurements()} to get them.
     *
     * @param event the parameter is passed from the xhtml as a event , sensor station is extracted
     *              from it.
     */
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            sensorStation = (SensorStation) event.getData();
            System.out.println(sensorStation);
            if (sensorStation != this.sensorStation) {
                getLatestMeasurements();
            }
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
        latestMeasurements = measurementService.getLatestPlantMeasurements(sensorStation);
        return latestMeasurements;
    }


    /**
     * Sets the currently displayed sensor station and reloads it form db. This sensor station is
     * targeted by any further calls of
     * {@link #doReloadSensorStation()}, {@link #doSaveSensorStation()} and
     * {@link #doDeleteSensorStation()}.
     */
    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
        doReloadSensorStation();
    }

    //TODO: Remove along the hierarchy and replace with something more elegant
    public void setSensorStationFromId(String id) {
        this.sensorStation = sensorService.loadSensorStation(id);
    }


    /**
     * Action to force a reload of the currently cached Sensor Station.
     */
    public void doReloadSensorStation() {
        sensorStation = sensorService.loadSensorStation(sensorStation.getId());
    }

    /**
     * Action to save the currently cached Sensor Station.
     */
    public void doSaveSensorStation() {
        System.out.println("im sensor detail controller im saving sensor station");
        if (fixed || sensorStation.getAlarmSwitch().equals("true")) sensorStation.setAlarmSwitch("fixed");
        if (gardeners != null) {this.doAddGardenersToSensorStation();}
        if (gardenersToRemove != null) {this.doRemoveGardenersFromSensorStation();}

        sensorStation = this.sensorService.saveSensorStation(sensorStation);
    }


    //TODO:Better error handling
    public void doChangeThePlantAndSave() {
        if (selectedPlantName != null){
            if(sensorStation.getPlant() != null){
                Plant oldPlant = plantService.loadPlant(sensorStation.getPlant().getId());
                oldPlant.setSensorStation(null);
                plantService.savePlant(oldPlant);
                Plant newPlant = new Plant();
                newPlant.setPlantName(selectedPlantName);
                newPlant.setSensorStation(sensorStation);
                sensorStation.setPlant(plantService.savePlant(newPlant));
                doSaveSensorStation();
            }else{
                Plant newPlant = new Plant();
                newPlant.setPlantName(selectedPlantName);
                newPlant.setSensorStation(sensorStation);
                sensorStation.setPlant(plantService.savePlant(newPlant));
                doSaveSensorStation();
            }
        }
        sensorStation = this.sensorService.saveSensorStation(sensorStation);
    }




    /**
     * Action to delete the currently cached Sensor Station.
     */
    public void doDeleteSensorStation() {
        this.sensorService.deleteSensorStation(sensorStation);
        sensorStation = null;
    }




    /**
     * Methods to get the images of the plant currently displayed in the sensor station.
     */
    public List<Image> doGetPlantImages() {
        if (plantId == null){return new ArrayList<Image>();}
        return galleryController.doGetPlantImages(plantId);
    }
    public List<Image> doGetPlantImagesNotYetApproved() {
        if (plantId == null){return new ArrayList<Image>();}
        return galleryController.doGetPlantImagesNotYetApproved(plantId);
    }
    /**
     * Method to get all approved images of the cached plantid
     * */
    public List<Image> doGetApprovedPlantImages() {//TODO fix the error handling like in sensor station detail controller the method with same name
        //if (idString == null){return new ArrayList<Image>();}
        //return imageService.getAllPlantImages(idString);
        return galleryController.doGetApprovedPlantImages(plantId);
    }






    //TODO: simplify this monstruosity

    /**
     * Method to initialize a greenhouse/sensor station view for a specific greenhouse taken from facescontext.
     */
    public void init() {
        if (this.sensorStation != null) return;
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        String idString = params.get("id");
        System.out.println("Im sensor station detail controller sensor station ID HERE:>" + idString); // testing ;D
        this.newSensorStation = false;
        if (idString == null) {
            this.newSensorStation = true;
            this.description = "";
            this.plantName = "";
            this.sensorStation = new SensorStation();
        } else {
            this.setSensorStationFromId(idString);
            this.sensorStation = this.getSensorStation();
            if (this.getSensorStation().getAlarmSwitch().equalsIgnoreCase("fixed")){this.fixed = true;}
            if (this.getSensorStation().getPlant() == null) {
                return;
            } // error handling XD
            this.plantName = "" + this.getSensorStation().getPlant().getPlantName();
            this.description = this.getSensorStation().getPlant().getDescription();
            this.plantId = this.getSensorStation().getPlant().getPlantID().toString();
        }

    }


}
