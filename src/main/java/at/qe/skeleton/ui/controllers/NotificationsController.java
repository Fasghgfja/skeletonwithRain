package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


@Getter
@Setter
@Component
@Scope("session")
public class NotificationsController {

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    @Autowired
    private transient SensorService sensorService;



    @Autowired
    private transient ImageService imageService;

    @Autowired
    private transient SensorStationService sensorStationService;




    private long alarmcount;
    private long picCount;



    private boolean hasShownPicsAwaitingApproval = false;

    private boolean hasShownAssignedAlarms = false;

    public boolean isHasShownPicsAwaitingApproval() {
        return hasShownPicsAwaitingApproval;
    }

    public void setHasShownPicsAwaitingApproval(boolean hasShownPicsAwaitingApproval) {
        this.hasShownPicsAwaitingApproval = hasShownPicsAwaitingApproval;
    }




    public boolean isHasShownAssignedAlarms() {
        return hasShownAssignedAlarms;
    }
    public void setHasShownAssignedAlarms(boolean hasShownAssignedAlarms) {
        this.hasShownAssignedAlarms = hasShownAssignedAlarms;
    }



    public long getAssignedSensorStationsAlarms() {//TODO: try if it is still possible resource wise to call this on every page refresh
        if(this.isHasShownAssignedAlarms() == false) {

            HashMap<SensorStation,Integer> assignedSensorStationMap = new HashMap<>();
            sessionInfoBean.getCurrentUser().getSensorStationsUnderCare().forEach(x->{
                    assignedSensorStationMap.put(x,x.getAlarmCountThreshold());});

            ArrayList<Sensor> sensors = new ArrayList<>();

            assignedSensorStationMap.forEach((x,y) -> {
                sensors.addAll(sensorService.getAllSensorsBySensorStation(x));});
           alarmcount = 0;

                  sensors.forEach(x ->{
                        Integer xtreshold = assignedSensorStationMap.get(x.getSensorStation());
                        if(xtreshold == null || x.getAlarm_count() >= xtreshold) {alarmcount++;}
                          });
            this.setHasShownAssignedAlarms(true);
            return alarmcount;
        }
        else return alarmcount;
    }


    public long getPicturesAwaitingApprovalCount() {//TODO:  try if it is still possible resource wise to call this on every page refresh
        if(this.isHasShownPicsAwaitingApproval() == false) {
            HashSet<Plant> plantsUnderCare = new HashSet<>();
            sessionInfoBean.getCurrentUser().getSensorStationsUnderCare().forEach(x->plantsUnderCare.add(x.getPlant()));
            plantsUnderCare.forEach(x->picCount += imageService.getNotApprovedImagesAmountForPlant(x));
            this.setHasShownPicsAwaitingApproval(true);

            return picCount;
        }
        else return picCount;

    }






}
