package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;

import java.io.Serializable;
import java.util.*;

import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
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
public class PlantPhotoGalleryController implements Serializable {


    @Autowired
    private transient PlantService plantService;
    @Autowired
    private transient ImageService imageService;
    @Autowired
    private GalleryController galleryController;
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

    private String plantId = "";


    /**
     * The methods gets all images of the cached idString
     */
    public List<Image> doGetPlantImages() {
        return galleryController.doGetPlantImages(idString);
    }


    /**
     * Method to get all images of the cached plantid
     */
    public List<Image> doGetApprovedPlantImages() {
        return galleryController.doGetApprovedPlantImages(plantId);
    }


    public void setPlant(Plant plant) {
        this.plant = plant;
        doReloadPlant();
    }

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
     * Method to initialize a greenhouse/sensor station view for a specific greenhouse taken from facescontext.
     */
    public void init() {
        if (this.plant != null) return;
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        idString = params.get("id");
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
            }
            this.plantName = "" + this.getPlant().getPlantName();
            this.description = this.getPlant().getDescription();
            this.plantId = this.getPlant().getPlantID().toString();
        }

    }
}
