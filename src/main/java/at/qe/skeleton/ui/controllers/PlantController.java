package at.qe.skeleton.ui.controllers;
import at.qe.skeleton.model.Plant;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.PlantService;
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
    public void doDeletePlant() {
        this.plantService.deletePlant(plant);
        plant = null;
    }



}
