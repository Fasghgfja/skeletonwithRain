package at.qe.skeleton.ui.beans;


import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorService;
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

@Getter
@Setter
@Component
@Scope("request")
public class SearchBean {

    @Autowired
    private transient PlantService plantService;

    @Autowired
    private transient SensorStationService sensorStationService;

    @Autowired
    private transient UserService userService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    private String plantNameToSearchFor;


    public void search() throws IOException {//TODO:  try if it is still possible resource wise to call this on every page refresh
        Plant thisPlant = plantService.findFirstByName(plantNameToSearchFor);
        if (plantNameToSearchFor == null){return;}
        if (thisPlant != null) {
            if (sessionInfoBean.hasRole("GARDENER") || sessionInfoBean.hasRole("ADMIN")) {
                System.out.println("imcalled");
                HashSet<SensorStation> assignedSS = new HashSet<SensorStation>();
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
                //im im a user
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();
                externalContext.redirect(externalContext.getRequestContextPath() + "/plantPage.xhtml?id=" + thisPlant.getPlantID());
            }
        } else {
            System.out.println("im showing the messae");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Plant Not Found", " "));
        }
    }





}



   /** @FacesValidator(" at.qe.skeleton.ui.beans.UrlValidator")
    public static class MyValidator implements Validator {


        @Override
        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

            StringBuilder url = new StringBuilder();
            String urlValue = value.toString();

            if(!urlValue.startsWith("http://", 0)) {
                url.append("http://");
            }
            url.append(urlValue);

            try {
                new URI(url.toString());
            } catch (URISyntaxException e) {
                FacesMessage msg =
                        new FacesMessage("URL validation failed","Invalid URL format");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);



        }

        }

    }
    */