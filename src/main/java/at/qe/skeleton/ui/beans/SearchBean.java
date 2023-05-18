package at.qe.skeleton.ui.beans;


import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.HashSet;

/**
 * This class is used to implement the Search bar in the topbar of the webapp.
 * Searching can be done by entering a plant name. If the plant with the entered name is not stored in the database, an error message will be displayed.
 */
@Getter
@Setter
@Component
@Scope("request")
public class SearchBean {

    @Autowired
    private PlantService plantService;

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private String plantNameToSearchFor;


    public void search() throws IOException {
        Plant thisPlant = plantService.findFirstByName(plantNameToSearchFor);
        if (plantNameToSearchFor == null){return;}
        if (thisPlant != null) {
            if (sessionInfoBean.hasRole("GARDENER") || sessionInfoBean.hasRole("ADMIN")) {
                HashSet<SensorStation> assignedSS = new HashSet<>();
                assignedSS.addAll(sensorStationService.getAllAssignedSensorStations(sessionInfoBean.getCurrentUser()));
                HashSet<Plant> assignedPlants = new HashSet<>();
                assignedSS.forEach(x -> assignedPlants.add(x.getPlant()));
                if (assignedPlants.contains(thisPlant)) {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = facesContext.getExternalContext();
                    externalContext.redirect(externalContext.getRequestContextPath() + "/greenHouseDetails.xhtml?id=" + thisPlant.getSensorStation().getSensorStationName());
                } else {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = facesContext.getExternalContext();
                    externalContext.redirect(externalContext.getRequestContextPath() + "/plantPage.xhtml?id=" + thisPlant.getPlantID());
                }
            } else {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();
                externalContext.redirect(externalContext.getRequestContextPath() + "/plantPage.xhtml?id=" + thisPlant.getPlantID());
            }
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Plant Not Found", " "));
        }
    }
}