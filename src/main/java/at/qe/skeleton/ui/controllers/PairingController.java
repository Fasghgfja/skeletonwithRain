package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Getter
@Setter
@Component
@Scope("view")
public class PairingController {


    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    AccessPointService accessPointService;


    @Autowired
    SensorService sensorService;


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

        //for loop
        //if sensor station sensorservice.threisdata(sensorstation) answers true we have sensors for this sensor station
        //pairing is succeeed else pairing failed
        //search for up to 5 minutes to see if you get any information


        //TODO: pairing logic here;
        SensorStation sensorStationToPair = sensorStationService.loadSensorStation(sensorStationId);
        AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
        sensorStationToPair.setAccessPoint(accessPointToPair);
        sensorStationService.saveSensorStation(sensorStationToPair);
    }


    private volatile boolean interruptFlag = false;
    public boolean isPairingInProgress = false;
    public void abortPairing(){
        System.out.println("im aborting pairing");
        interruptFlag = true;
    }

    public void pairTest() {//TODO new!
        System.out.println("I'm pairing controller new! and I'm executing the pairing logic");
        SensorStation sensorStationToPair = sensorStationService.loadSensorStation(sensorStationId);

        // Set the maximum time to search for sensors to 5 minutes (300,000 milliseconds)
        final long MAX_SEARCH_TIME_MS = 300000;

        long startTime = System.currentTimeMillis();
        boolean sensorsFound = false;
        while (!sensorsFound && (System.currentTimeMillis() - startTime < MAX_SEARCH_TIME_MS) && !interruptFlag) {
            sensorsFound = sensorService.areSensorsPresent(sensorStationToPair );
            System.out.println(sensorService.getAllSensorsBySensorStation(sensorStationToPair)+ " " + sensorService.areSensorsPresent(sensorStationToPair));
            if (!sensorsFound) {
                try {
                    System.out.println("im waiting 5 seconds before checking again");
                    if(interruptFlag){break;}
                    System.out.println("interurpt flag is" + interruptFlag);
                    // Wait for 5 seconds before checking again
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (sensorsFound) {
                System.out.println("i found sensors so im pairing");
                AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
                sensorStationToPair.setAccessPoint(accessPointToPair);
                sensorStationService.saveSensorStation(sensorStationToPair);
                break;
            }
        }

    }



    public void unpair(){
        System.out.println("im pairing controller and im executing the UNpairing logic");
        //TODO: UNpairing logic here;
        SensorStation sensorStationToUnpair = sensorStationService.loadSensorStation(sensorStationId);
        sensorStationToUnpair.setAccessPoint(null);
        sensorStationService.saveSensorStation(sensorStationToUnpair);
    }



}
