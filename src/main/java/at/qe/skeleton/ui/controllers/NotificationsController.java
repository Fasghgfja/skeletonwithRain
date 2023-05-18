package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class is used to show notifications for the logged-in user, such as the number of pictures awaiting approval
 * or the number of alarms that have gone off for the responsible sensor stations.
 */

@Getter
@Setter
@Component
@Scope("session")
public class NotificationsController {

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private SensorService sensorService;


    @Autowired
    private ImageService imageService;

    @Autowired
    private SensorStationService sensorStationService;


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


    public long getAssignedSensorStationsAlarms() {
        if (!this.isHasShownAssignedAlarms()) {

            HashMap<SensorStation, Integer> assignedSensorStationMap = new HashMap<>();
            sessionInfoBean.getCurrentUser().getSensorStationsUnderCare().forEach(x -> assignedSensorStationMap.put(x, x.getAlarmCountThreshold()));

            ArrayList<Sensor> sensors = new ArrayList<>();

            assignedSensorStationMap.forEach((x, y) -> sensors.addAll(sensorService.getAllSensorsBySensorStation(x)));
            alarmcount = 0;

            sensors.forEach(x -> {
                Integer xtreshold = assignedSensorStationMap.get(x.getSensorStation());
                if (xtreshold == null || x.getAlarm_count() >= xtreshold) {
                    alarmcount++;
                }
            });
            this.setHasShownAssignedAlarms(true);
            return alarmcount;
        } else return alarmcount;
    }


    public long getPicturesAwaitingApprovalCount() {
        if (!this.isHasShownPicsAwaitingApproval()) {
            HashSet<Plant> plantsUnderCare = new HashSet<>();
            sessionInfoBean.getCurrentUser().getSensorStationsUnderCare().forEach(x -> plantsUnderCare.add(x.getPlant()));
            plantsUnderCare.forEach(x -> picCount += imageService.getNotApprovedImagesAmountForPlant(x));
            this.setHasShownPicsAwaitingApproval(true);

            return picCount;
        } else return picCount;

    }
}
