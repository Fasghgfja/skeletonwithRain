package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Controller for the pairing of a sensor station to an access point.
 */

@Getter
@Setter
@Component
@Scope("view")
public class PairingController {

    @Autowired
    private SensorStationService sensorStationService;
    @Autowired
    private AccessPointService accessPointService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SessionInfoBean sessionInfoBean;
    @Autowired
    private LogRepository logRepository;


    private final Logger successLogger = Logger.getLogger("SuccessLogger");
    private FileHandler successFileHandler;

    private String sensorStationId = "";
    private Long accessPointId;
    private boolean sensorsFound;
    private volatile boolean interruptFlag = false;


    public List<Long> getAccessPointsIds () {
        return accessPointService.getAllAccessPointIds();
    }

    public Long getSensorStationAccessPointId () {
        SensorStation thisSensorStation = sensorStationService.loadSensorStation(sensorStationId);
        if (thisSensorStation == null) {return null;}
        if (thisSensorStation.getAccessPoint() == null) {return null;}
        return thisSensorStation.getAccessPoint().getAccessPointID();
    }


    /**
     * Method to pair a sensor station to an access point. This method will try pairing for at most 5 minutes before it aborts.
     * If the method aborts no access point will be paired to the sensor station.
     * Otherwise the sensor station is paired to access point and can start sending data.
     */

    public void pair() {
        SensorStation sensorStationToPair = sensorStationService.loadSensorStation(sensorStationId);
        AccessPoint accessPointToPair = accessPointService.loadAccessPoint(accessPointId);
        sensorStationToPair.setAccessPoint(accessPointToPair);
        sensorStationService.saveSensorStation(sensorStationToPair);
        // Set the maximum time to search for sensors to 5 minutes (300,000 milliseconds)
        final long MAX_SEARCH_TIME_MS = 300000;

        long startTime = System.currentTimeMillis();
        sensorsFound = false;
        interruptFlag = false;
        while (!sensorsFound && (System.currentTimeMillis() - startTime < MAX_SEARCH_TIME_MS) && !interruptFlag) {
            sensorsFound = sensorService.areSensorsPresent(sensorStationToPair );
            if (!sensorsFound) {
                try {
                    if(interruptFlag){
                        interruptFlag = false;
                        break;}
                    // Wait for 5 seconds before checking again
                    Thread.sleep(4000);
                    System.out.println("Sleep");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            if (sensorsFound) {
                try {
                    successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                    successFileHandler.setFormatter(new SimpleFormatter());
                    successLogger.addHandler(successFileHandler);
                    successLogger.info("SENSOR STATION PAIRED : " + sensorStationToPair.getSensorStationID());
                    successFileHandler.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log createLog = new Log();
                createLog.setDate(LocalDate.now());
                createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
                createLog.setSubject("SENSOR STATION PAIRED");
                createLog.setText("SENSOR STATION PAIRED: " + sensorStationToPair.getSensorStationID());
                createLog.setType(LogType.SUCCESS);
                logRepository.save(createLog);
                break;
            }
        }
        abortPairing();
    }

    /**
     * Removes the pairing of a sensor station to an access point and sets it to null.
     */

    public void unpair(){
        SensorStation sensorStationToUnpair = sensorStationService.loadSensorStation(sensorStationId);
        sensorStationToUnpair.setAccessPoint(null);
        sensorStationService.saveSensorStation(sensorStationToUnpair);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("UNPAIRED : " + sensorStationToUnpair.getSensorStationID());
            successFileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
        createLog.setSubject("SENSOR STATION UNPAIRED");
        createLog.setText("SENSOR STATION UNPAIRED: " + sensorStationToUnpair.getSensorStationID());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }

    /**
     * Method to abort the pairing. Since the pairing is implemented to try for 5 minutes, the user can use this method to abort
     * the pairing earlier.
     */
    public void abortPairing(){
        if(!sensorsFound) {
            SensorStation sensorStationToDeleteAccessPoint =
                    sensorStationService.loadSensorStation(sensorStationId);
            sensorStationToDeleteAccessPoint.setAccessPoint(null);
            sensorStationService.saveSensorStation(sensorStationToDeleteAccessPoint);
        }
        interruptFlag = true;
    }
}
