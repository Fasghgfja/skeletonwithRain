package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.model.*;
import java.io.Serializable;
import java.util.*;
import at.qe.skeleton.services.*;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Collection<Measurement> latestMeasurements;
    private String plantName = "";
    private String plantId;
    private String description = "";
    private Plant plant;
    private String selectedPlantName;

    private Plant selectedPlant; //TODO: new

    boolean fixed = false;


    //TODO: simplify this to user javax unselect event to just remove the gardenrs with only one checkbox menu use real user and not strings!
    List<String> gardeners;
    List<Userx> gardenersToRemove;//TODO:this is the correct way not strings!

    AccessPoint accessPoint;



    public String getSelectedPlantName() {
        return selectedPlantName;
    }

    public void setSelectedPlantName(String selectedPlantName) {
        this.selectedPlantName = selectedPlantName;
    }



    public void doAddGardenerToSensorStation(String user) {
        this.sensorService.addGardenerToSensorStation(sensorStation,user);
    }
    public void doAddGardenersToSensorStation() {//TODO:use this
        gardeners.forEach(this::doAddGardenerToSensorStation);
    }
    public void doRemoveGardenerFromSensorStation(Userx user) {
        this.sensorService.removeGardenerFromSensorStation(sensorStation,user);
    }
    public void doRemoveGardenersFromSensorStation() {
        gardenersToRemove.forEach(this::doRemoveGardenerFromSensorStation);
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
            if (sensorStation != this.sensorStation) {
                getLatestMeasurements();
            }
        }
    }
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
    public void setSensorStationFromId(String id) {
        this.sensorStation = sensorService.loadSensorStation(id);
    }


    //TODO:Better error handling , put error handling at beginning and remove nested ifs
    public void doChangeThePlantAndSave() {
        if (selectedPlantName != null && !selectedPlantName.equals("")){
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


    public void doDeleteSensorStation() {
        this.sensorService.deleteSensorStation(sensorStation);
        sensorStation = null;
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
