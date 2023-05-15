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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    private String alarmcount;

    public String getAssignedSensorStationsAlarms() {//TODO: verify how expensive this is resourcewise and maybe pushdfown to service layer
       Set<SensorStation> assignedSensorList = sessionInfoBean.getCurrentUser().getSensorStationsUnderCare();
        System.out.println("im called");
       HashSet<Sensor> sensors = new HashSet<>();
       assignedSensorList.forEach(x->sensors.addAll(sensorService.getAllSensorsBySensorStation(x)));
       long count = sensors.stream().filter(x->x.getAlarm_count() > 4).count();
       return  alarmcount = count + "";
    }


    public String getPicturesAwaitingApprovalCount() {//TODO: verify how expensive this is resourcewise and maybe pushdfown to service layer
        return  "";
    }


}
