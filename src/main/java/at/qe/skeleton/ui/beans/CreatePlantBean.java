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

    //TODO: add planted date;
    //TODO: add logging

    public void doCreateNewPlant(){
        Plant plant = new Plant();
        plant.setPlantName(plantName);
        plant.setDescription(description);
        //TODO: add planted date
        plant.setDescription(description);
        plant = plantService.savePlant(plant);

        //TODO: log sensor station creation
        //Log createLog = new Log();
        //createLog.setDate(LocalDate.now());
        // createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        //createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
        // createLog.setSubject("AP CREATION");
        //createLog.setText("CREATED AP: " + sensorStation.getId());
        //createLog.setType(LogType.SUCCESS);
        //logRepository.save(createLog);

    }
}
