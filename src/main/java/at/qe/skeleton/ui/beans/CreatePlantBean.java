package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.PlantService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class is used to create new plants from the plant management page. This page is only available to users with the role "ADMIN".
 */
@Getter
@Setter
@Component
@Scope("view")
public class CreatePlantBean implements Serializable {

    @Autowired
    transient PlantService plantService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;

    private String plantName;
    private String description;

    public void doCreateNewPlant() {
        Plant plant = new Plant();
        plant.setPlantName(plantName);
        plant.setDescription(description);
        plant = plantService.savePlant(plant);

        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("CREATED PLANT: " + plant.getPlantID());
            successFileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
