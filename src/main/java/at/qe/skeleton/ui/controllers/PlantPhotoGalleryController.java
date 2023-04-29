package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.*;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.qe.skeleton.repositories.AbstractRepository;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


//TODO: merge this with galleryController or the other way around register.xhtml uses this

/**
 * Controller for the sensor stations detail view.
 */
@Getter
@Setter
@Component
@Scope("application")
public class PlantPhotoGalleryController implements Serializable {


    /**
     * Autowired dependencies.
     * Spring will automatically resolve and inject a matching bean from the Spring application context at runtime.
     */
    @Autowired
    private PlantService plantService;


    @Autowired
    private ImageService imageService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;



    /**
     * Tells us if the sensor station is new , replace with a more elegant solution!.
     */
    private boolean newPlant;

    /**
     * Attribute to cache the currently displayed sensor station.
     */
    private Plant plant;

    /**
     * maybe not needed fields.
     */
    private String plantName = "";
    private String description = "";

    private String idString = "";




    public List<Image> doGetPlantImages() {
        return imageService.getAllPlantImages(idString);
    }




    public ByteArrayInputStream getPhotoAsStreamedContent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            byte []a = new byte[0];
            return new ByteArrayInputStream(a);
        } else {
            String imageId = context.getExternalContext().getRequestParameterMap().get("id");
            Image image = imageService.loadImage(Long.valueOf(imageId));
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        }
    }







//TODO: update the javadoc of this page

    /**
     * Sets the currently displayed sensor station and reloads it form db. This sensor station is
     * targeted by any further calls of
     * {@link #doReloadSensorStation()}, {@link #doSaveSensorStation()} and
     * {@link #doDeleteSensorStation()}.
     */
    public void setPlant(Plant plant) {
        this.plant = plant;
        doReloadPlant();
    }

    //TODO: Remove along the hierarchy and replace with something more elegant
    public void setPlantFromId(Long id) {
        this.plant = plantService.loadPlant(id);
    }


    /**
     * Action to force a reload of the currently cached Sensor Station.
     */
    public void doReloadPlant() {
        plant = plantService.loadPlant(plant.getId());
    }

    /**
     * Action to save the currently cached Sensor Station.
     */
    public void doSavePlant() {
        plant = this.plantService.savePlant(plant);
    }

    /**
     * Action to delete the currently cached Sensor Station.
     */
    public void doDeletePlant() {
        this.plantService.deletePlant(plant);
        plant = null;
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
        idString = params.get("id");
        System.out.println("Im PLANTH PHOTO GALLERY CONTROLLER ID HERE:--------------->" + idString); // testing ;D
        this.newPlant = false;
        if (idString == null) {
            this.newPlant = true;
            this.description = "";
            this.plantName = "";
            this.plant = new Plant();
        } else {
            this.setPlantFromId(Long.parseLong(idString));
            this.plant = this.getPlant();
            if (this.getPlant() == null) {
                return;
            } // error handling XD
            this.plantName = "" + this.getPlant().getPlantName();
            this.description = this.getPlant().getDescription();
        }

    }


}
