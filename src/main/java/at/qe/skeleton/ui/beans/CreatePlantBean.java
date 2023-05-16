package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Component
@Scope("view")
public class CreatePlantBean implements Serializable {

    @Autowired transient PlantService plantService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;


    private String plantName;
    private String description;

    public void doCreateNewPlant(){
        Plant plant = new Plant();
        plant.setPlantName(plantName);
        plant.setDescription(description);
        plant = plantService.savePlant(plant);

        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
        createLog.setSubject("PLANT CREATION");
        createLog.setText("CREATED PLANT: " + plant.getPlantID());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }
}
