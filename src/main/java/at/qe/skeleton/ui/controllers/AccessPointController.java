package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.LogService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
@Setter
@Component
@Scope("view")
public class AccessPointController implements Serializable {

    private List<AccessPoint> accessPointList;

    @Autowired
    private transient AccessPointService accessPointService;


    @Autowired
    private transient SensorStationService sensorStationService;

    @Autowired
    private transient IntervalService intervalService;


    @Autowired
    private transient LogService logService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AccessPointController.class);

    private AccessPoint accessPoint;
    private String location;

    /**
     * Constructs a list of all access points in the database for sorting and filtering on the datatable.
     */

    @PostConstruct
    public void init(){
        accessPointList = (ArrayList<AccessPoint>) accessPointService.getAllAccessPoint();
    }

    /**
     * Method to get all access points stored in the database.
     * @return Collection of all access points in the database.
     */
    public Collection<AccessPoint> getAccessPoints(){
        return accessPointService.getAllAccessPoint();
    }

    /**
     * Returns the amount of all exisiting access points.
     */
    public long getAccessPointsAmount(){
        return accessPointService.getAccessPointsAmount();
    }

    /**
     * Deletes an access point.
     */

    public void doDeleteAccessPoint(){
        sensorStationService.removeAccessPointFromSensorStations(accessPoint);

        accessPointService.deleteAccessPoint(accessPoint);
        Log deleteLog = new Log();
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(sessionInfoBean.getCurrentUserName());
        deleteLog.setSubject("AP DELETION");
        deleteLog.setText("DELETED AP: " + accessPoint.getId());
        deleteLog.setType(LogType.SUCCESS);
        logService.saveLog(deleteLog);
        try {
            if (successFileHandler == null) {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
            }
            successLogger.info("DELETED AP: " + accessPoint.getAccessPointID());
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
        accessPoint = null;

    }

    /**
     * Method to validate an existing access point, since access points that are created are never validated automatically.
     */

    public void doValidateAccessPoint(){
        accessPoint.setValidated(true);
        accessPointService.saveAccessPoint(accessPoint);
        try {
            if (successFileHandler == null) {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
            }
            successLogger.info("ACCESS POINT VALIDATED: " + accessPoint.getAccessPointID());
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
        Log validationLog = new Log();
        validationLog.setDate(LocalDate.now());
        validationLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        validationLog.setAuthor(sessionInfoBean.getCurrentUserName());
        validationLog.setSubject("AP VALIDATED");
        validationLog.setText("VALIDATED AP: " + accessPoint.getId());
        validationLog.setType(LogType.SUCCESS);
        logService.saveLog(validationLog);
    }
}

