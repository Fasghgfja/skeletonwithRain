package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;


@Getter
@Setter
@Component
@Scope("view")
public class NotificationsController {

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    @Autowired
    private transient SensorService sensorService;

    @Autowired
    private transient SensorStationService sensorStationService;

    private long alarmcount;

    public long getAssignedSensorStationsAlarms() {//TODO: lol test this if u can
        if(sessionInfoBean.isHasShownAssignedAlarms() == false) {
            HashMap<SensorStation,Integer> assignedSensorStationMap = new HashMap<>();
            sessionInfoBean.getCurrentUser().getSensorStationsUnderCare().forEach(x->{
                    assignedSensorStationMap.put(x,x.getAlarmCountThreshold());});

            ArrayList<Sensor> sensors = new ArrayList<>();
           // assignedSensorStationMap.forEach((x,y) -> {
              //  sensors.addAll(sensorService.getAllSensorsBySensorStation(x));});
        //   alarmcount = 0;
                  // sensors.forEach(x -> System.out.println(assignedSensorStationMap.get(x.getSensorStation())));
            sessionInfoBean.setHasShownAssignedAlarms(true);
            return alarmcount;
        }
        else return alarmcount;
    }


    public String getPicturesAwaitingApprovalCount() {//TODO:implement like the one above verify how expensive this is resourcewise and maybe pushdfown to service layer
        return  "";
    }


}
