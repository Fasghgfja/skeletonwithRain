package at.qe.skeleton.ui.controllers;
import at.qe.skeleton.model.Plant;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
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
    private SensorStationService sensorStationService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private Collection<Plant> filteredPlants;

    /**
     * Cached plant.
     */
    private Plant plant;

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



}
