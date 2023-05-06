package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;


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
    private volatile boolean interruptFlag = false;



    @PostConstruct
    public void init() {
    }


    public List<Long> getAccessPointsIds () {
        return accessPointService.getAllAccessPointIds();
    }//TODO: move this to AccessPointListController without breaking anything

    public Long getSensorStationAccessPointId () {
        SensorStation thisSensorStation = sensorStationService.loadSensorStation(sensorStationId);
        if (thisSensorStation == null) {return null;}
        if (thisSensorStation.getAccessPoint() == null) {return null;}
        return thisSensorStation.getAccessPoint().getAccessPointID();
    }



    public void pair() {
        System.out.println("I'm pairing controller new! and I'm executing the pairing logic");
        SensorStation sensorStationToPair = sensorStationService.loadSensorStation(sensorStationId);
        AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
        sensorStationToPair.setAccessPoint(accessPointToPair);
        sensorStationService.saveSensorStation(sensorStationToPair);
        // Set the maximum time to search for sensors to 5 minutes (300,000 milliseconds)
        final long MAX_SEARCH_TIME_MS = 300000;

        long startTime = System.currentTimeMillis();
        boolean sensorsFound = false;
        interruptFlag = false;
        while (!sensorsFound && (System.currentTimeMillis() - startTime < MAX_SEARCH_TIME_MS) && !interruptFlag) {
            sensorsFound = sensorService.areSensorsPresent(sensorStationToPair );
            System.out.println(sensorService.getAllSensorsBySensorStation(sensorStationToPair)+ " " + sensorService.areSensorsPresent(sensorStationToPair));
            if (!sensorsFound) {
                try {
                    System.out.println("im waiting 5 seconds before checking again");
                    if(interruptFlag){
                        interruptFlag = false;
                        break;}
                    System.out.println("interurpt flag is" + interruptFlag);
                    // Wait for 5 seconds before checking again
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (sensorsFound) {
                System.out.println("i found sensors so im pairing");

                System.out.println(interruptFlag);
                //AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
                //sensorStationToPair.setAccessPoint(accessPointToPair);
                //sensorStationService.saveSensorStation(sensorStationToPair);
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

    public void abortPairing(){
        System.out.println("im aborting pairing");
        interruptFlag = true;
    }


}
