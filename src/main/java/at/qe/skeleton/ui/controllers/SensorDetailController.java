package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import java.io.Serializable;
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
    private SensorService sensorService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    /**
     * Tells us if the sensor  is new , replace with a more elegant solution!.
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
        this.upper_border = sensor.getUpper_border().toString();
        this.lower_border = sensor.getLower_border().toString();
        upper_border = this.sensor.getUpper_border().toString();
    }

    public void doReloadSensor() {
        sensor = sensorService.loadSensor(sensor.getId());
    }

    /**
     * Action to save the currently cached Sensor Station.
     */
 //TODO: implement logging maybe? this is doen form the sensor station automatically (maybe)
    public void doSaveSensor() {
        if (resetAlarm != true) {
        sensor.setAlarm_count(0);
        }
        sensor.setUpper_border(upper_border);
        sensor.setLower_border(lower_border);
        if(resetAlarm){sensor.setAlarm_count(0);}
        sensor = this.sensorService.saveSensor(sensor);
        //Log createLog = new Log();
        //createLog.setDate(LocalDate.now());
        //createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        //createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        //createLog.setSubject("USER EDIT");
        //createLog.setText("EDITED USER: " + user.getUsername());
        //createLog.setType(LogType.SUCCESS);
        //logRepository.save(createLog);
    }

    /**
     * Action to delete the currently cached Sensor Station.
     */
    public void doDeleteSensor() {
        this.sensorService.deleteSensor(sensor);
        sensor = null;
    }



}
