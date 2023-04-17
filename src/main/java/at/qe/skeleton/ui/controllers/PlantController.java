package at.qe.skeleton.ui.controllers;
import at.qe.skeleton.model.Plant;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.services.PlantService;
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

    private Collection<Plant> filteredPlants;

    /**
     * Returns a list of all plants.
     */
    public Collection<Plant> getPlants() {
        return plantService.getAllPlants();
    }

    /**
     * Returns how many plants are registered in the system.
     */
    public Integer getPlantsAmount() {
        return plantService.getPlantsAmount();
    }

}
