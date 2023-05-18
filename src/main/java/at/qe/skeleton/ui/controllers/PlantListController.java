package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Component
@Scope("view")
public class PlantListController {

    private List<Plant> notYetFollowedPlantsList;

    @Autowired
    private SessionInfoBean sessionInfoBean;
    @Autowired
    private PlantService plantService;


    /**
     * The method is Only used in the scrolldown menu for plant selection.
     */

    @PostConstruct
    public void init() {
        Userx user = getSessionInfoBean().getCurrentUser();
        notYetFollowedPlantsList = (ArrayList<Plant>) plantService.getOnlyPlantsNotYetFollowed(user);
    }

    public Collection<String> getPlantsUniqueNames() {
        return plantService.getAllPlantsUniqueNames();
    }

    public Collection<String> getNotUsedPlantsUniqueNames() {
        return plantService.getAllNotUsedPlantsUniqueNames();
    }

    public Collection<Plant> getPlants() {
        return plantService.getAllPlants();
    }

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

    public void deleteAllPlantsWithoutSensorStation() {
        this.plantService.deleteAllPlantsWithoutSensorStation();
    }


}
