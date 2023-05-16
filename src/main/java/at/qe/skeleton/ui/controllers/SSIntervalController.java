package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;


@Getter
@Setter
@Component
@Scope("view")
public class SSIntervalController implements Serializable {

    @Autowired
    private transient IntervalService intervalService;
    @Autowired
    private transient AccessPointService accessPointService;


    private Integer measurementInterval;
    private Integer webAppInterval;

    private SSInterval sSinterval;
    private Long accessPointId;


    public void doSaveInterval(){
        sSinterval = new SSInterval();
        AccessPoint accessPoint = accessPointService.loadAccessPoint(accessPointId);
        SSInterval oldInterval = intervalService.getFirstByAccessPointId(accessPointId);

        System.out.println(accessPoint+""+webAppInterval+""+measurementInterval);
        if(oldInterval == null) {
            sSinterval.setAccessPoint(accessPoint);
            if(measurementInterval != null ) {sSinterval.setMeasurementInterval(measurementInterval.toString());}
            if(webAppInterval != null ) {sSinterval.setWebAppInterval(webAppInterval.toString());}
            sSinterval = this.intervalService.saveInterval(sSinterval);
        }
        else {
            if(measurementInterval != null ) {oldInterval.setMeasurementInterval(measurementInterval.toString());}
            if(webAppInterval != null ) {oldInterval.setWebAppInterval(webAppInterval.toString());}
            this.intervalService.saveInterval(oldInterval);
        }
    }

    public String getActualMeasurementIntervalForAccessPoint(Long tAccessPointId){
        AccessPoint accessPoint = accessPointService.loadAccessPoint(tAccessPointId);
        SSInterval oldInterval = intervalService.getFirstByAccessPointId(tAccessPointId);
        if(oldInterval != null){
            if(oldInterval.getMeasurementInterval() == null){return "Default";}
            return oldInterval.getMeasurementInterval();}
        else return "Default";
    }

    public String getActualWebAppIntervalForAccessPoint(Long tAccessPointId){
        SSInterval oldInterval = intervalService.getFirstByAccessPointId(tAccessPointId);
        if(oldInterval != null){
            if (oldInterval.getWebAppInterval() == null) {return "Default";}
            return oldInterval.getWebAppInterval();}
        else return "Default";
    }




}



