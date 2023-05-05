package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@Component
@Scope("view")
public class PairingController {


    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    AccessPointService accessPointService;


    String sensorStationId = "";

    Long accessPointId;


    @PostConstruct
    public void init() {

    }



    public List<Long> getAccessPointsIds () {
        return accessPointService.getAllAccessPointIds();
    }

    public Long getSensorStationAccessPointId () {
        SensorStation thisSensorStation = sensorStationService.loadSensorStation(sensorStationId);
        if (thisSensorStation == null) {return null;}
        if (thisSensorStation.getAccessPoint() == null) {return null;}
        return thisSensorStation.getAccessPoint().getAccessPointID();
    }

    public void pair(){
        System.out.println("im pairing controller and im executing the pairing logic");
        //TODO: pairing logic here;
        SensorStation sensorStationToPair = sensorStationService.loadSensorStation(sensorStationId);
        AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
        sensorStationToPair.setAccessPoint(accessPointToPair);
        sensorStationService.saveSensorStation(sensorStationToPair);
    }



    public void unpair(){
        System.out.println("im pairing controller and im executing the UNpairing logic");
        //TODO: UNpairing logic here;
        SensorStation sensorStationToUnpair = sensorStationService.loadSensorStation(sensorStationId);
        sensorStationToUnpair.setAccessPoint(null);
        sensorStationService.saveSensorStation(sensorStationToUnpair);
    }



}
