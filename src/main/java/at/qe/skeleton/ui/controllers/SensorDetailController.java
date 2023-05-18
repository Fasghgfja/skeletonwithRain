package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Controller for the sensor stations detail view.
 */
@Getter
@Setter
@Component
@Scope("view")
public class SensorDetailController implements Serializable {


    @Autowired
    private transient SensorService sensorService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;

    /**
     * Tells us if the sensor is new.
     */
    private boolean newSensor;

    /**
     * Attribute to cache the currently displayed sensor.
     */
    private Sensor sensor;
    private String upper_border;
    private String lower_border;
    boolean resetAlarm = false;


    public void setSensor(Sensor sensor) {
        this.sensor = sensorService.loadSensor(sensor.getId());
        this.upper_border = sensor.getUpper_border();
        this.lower_border = sensor.getLower_border();
        upper_border = this.sensor.getUpper_border();
    }

    public void doReloadSensor() {
        sensor = sensorService.loadSensor(sensor.getId());
    }

    /**
     * Action to save the currently cached Sensor Station.
     */
    public void doSaveSensor() {
        if (resetAlarm) {sensor.setAlarm_count(0);}
        sensor.setUpper_border(upper_border);
        sensor.setLower_border(lower_border);
        if(resetAlarm){sensor.setAlarm_count(0);}
        sensor = this.sensorService.saveSensor(sensor);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("SENSOR CREATED: " + sensor.getId());
            successFileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("SENSOR CREATED");
        createLog.setText("SENSOR CREATED: " + sensor.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }

    /**
     * Action to delete the currently cached Sensor Station.
     */
    public void doDeleteSensor() {
        this.sensorService.deleteSensor(sensor);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("SENSOR DELETED: " + sensor.getId());
            successFileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log deleteLog = new Log();
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(sessionInfoBean.getCurrentUserName());
        deleteLog.setSubject("SENSOR DELETION");
        deleteLog.setText("SENSOR DELETED: " + sensor.getId());
        deleteLog.setType(LogType.SUCCESS);
        logRepository.save(deleteLog);
        sensor = null;
    }
}
