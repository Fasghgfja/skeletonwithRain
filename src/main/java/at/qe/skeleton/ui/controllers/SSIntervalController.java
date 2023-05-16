package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Component
@Scope("view")
public class SSIntervalController implements Serializable {

    @Autowired
    private transient IntervalService intervalService;
    @Autowired
    private transient SensorStationService sSservice;


    private Integer minterval;
    private Integer sinterval;

    private SSInterval sSinterval;
    private String sensorStationId = "";


    public void doSaveInterval(){
        sSinterval = new SSInterval();
        SensorStation sensorStation = sSservice.loadSensorStation(sensorStationId);
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(sensorStationId);

        if(oldInterval == null) {
            sSinterval.setSensorStation(sensorStation);
            if(minterval != null ) {sSinterval.setMInterval(minterval.toString());}
            if(sinterval != null ) {sSinterval.setSInterval(sinterval.toString());}
            sSinterval = this.intervalService.saveInterval(sSinterval);
        }
        else {
            if(minterval != null ) {oldInterval.setMInterval(minterval.toString());}
            if(sinterval != null ) {oldInterval.setSInterval(sinterval.toString());}
            oldInterval = this.intervalService.saveInterval(oldInterval);
        }
    }

    public String getActualMIntervalForSensorStation(String tsensorStationId){
        SensorStation sensorStation = sSservice.loadSensorStation(tsensorStationId);
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(tsensorStationId);
        if(oldInterval != null){
            return oldInterval.getMInterval();}
        else return "Default";
    }

    public String getActualSIntervalForSensorStation(String tsensorStationId){
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(tsensorStationId);
        if(oldInterval != null){
            return oldInterval.getSInterval();}
        else return "Default";
    }




}



