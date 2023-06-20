package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.*;
import at.qe.skeleton.api.services.SensorStationServiceApi;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@RestController
public class SensorStationApiController {


    @Autowired
    SensorStationServiceApi sensorStationServiceApi;
    private final transient Logger sensorStationLogger = Logger.getLogger("Rest Api Sensor Station Logger");
    /**
     * This method is called to update SensorStations via Accesspoint
     * update values: descritption, alarm_switch
     * @param sensorStationApi
     * @return Response OK
     */
    @PostMapping("/api/sensorstations")
    int createSensorStationApi(@RequestBody SensorStationApi sensorStationApi) {
        try {
            sensorStationServiceApi.updateSensorStation(sensorStationApi);
            sensorStationLogger.info("SENSOR STATION HAS BEEN UPDATED: " + sensorStationApi.getName() + " " + sensorStationApi.getService_description());
            return HttpServletResponse.SC_OK;
        } catch (SensorStationNotFoundException ex){
            sensorStationLogger.warning("SENSOR STATION COULD NOT BE UPDATED: " + sensorStationApi.getName() + " " + sensorStationApi.getService_description());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * This method is called to save new sensors
     * @param sensorApi
     * @return response ok
     */
    @PostMapping("/api/sensors")
    int createSensorApi(@RequestBody List<SensorApi> sensorApi) {
        try {
            sensorStationServiceApi.addSensor(sensorApi);
            sensorApi.stream().findAny().ifPresent(sensorApi1->{
                sensorStationLogger.info("SENSORS ADDED FOR STATION: " + sensorApi1.getStation_name());
            });
            return HttpServletResponse.SC_OK;
        }catch (SensorNotFoundException ex) {
            sensorApi.stream().findAny().ifPresent(sensorApi1->{
                sensorStationLogger.warning("SENSORS COULD NOT BE ADDED FROM STATION: " + sensorApi1.getStation_name());
            });
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/updatesensors")
    int updateSensorApi(@RequestBody List<SensorApi> sensorApi) {
        try {
            sensorStationServiceApi.updateSensor(sensorApi);
            sensorApi.stream().findAny().ifPresent(sensorApi1->{
                sensorStationLogger.info("SENSORS UPDATED FOR STATION: " + sensorApi1.getStation_name());
            });
            return HttpServletResponse.SC_OK;
        }catch (SensorStationNotFoundException ex){
            sensorApi.stream().findAny().ifPresent(sensorApi1->{
                sensorStationLogger.warning("SENSORS COULD NOT BE UPDATED FOR STATION: " + sensorApi1.getStation_name());
            });
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/api/auditLog")
    int logFile(@RequestBody List<LogFrame> logFrame) {
        try{
            sensorStationServiceApi.saveLog(logFrame);
            logFrame.stream().findAny().ifPresent(logFrame1->{
                sensorStationLogger.info("LOG ADDED TO WEBAPP DATABASE OF ACCESS POINT: " + logFrame1.getAuthor());
            });
            return HttpServletResponse.SC_OK;
        }catch (SensorStationNotFoundException ex){
            logFrame.stream().findAny().ifPresent(logFrame1->{
                sensorStationLogger.warning("LOG COULD NOT ADDED TO WEBAPP DATABASE OF ACCESS POINT: " + logFrame1.getAuthor());
            });
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/newdevices/{id}")
    int nearbyDevices(@PathVariable("id") String id, @RequestBody List<SensorDevice> devices) {
        try{
            sensorStationServiceApi.saveNearbyDevices(Long.valueOf(id), devices);
            sensorStationLogger.info("NEARBY DEVICES ARE SEND BY ACCESS POINT: " + id);
            return HttpServletResponse.SC_OK;
        }catch (AccessPointNotFoundException ex){
            sensorStationLogger.warning("EARBY DEVICES COULD NOT SEND BY ACCESS POINT: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * This method is called to get the alarm_switch of a given SensorStation
     * @param sensorStationApi
     * @return
     */
    @GetMapping("/api/getsensorstations")
    String getOneSensorStationApi(@RequestBody SensorStationApi sensorStationApi) {
        try {
            return sensorStationServiceApi.findOneSensorStation(sensorStationApi.getName());
        } catch (SensorStationNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    //Added to call all sensorstations via rest
    @GetMapping("/api/sensorstations/{id}")
    List<String> getAllSensorStationApi(@PathVariable("id") String id) {
        try {
            List<String> returnList = sensorStationServiceApi.findAllSensorStation(Long.valueOf(id));
            sensorStationLogger.info("STATION NAMES CALLED BY ACCESS POINT: " + id);
            return returnList;
        } catch (SensorStationNotFoundException ex) {
            sensorStationLogger.warning("STATION NAMES COULD NOT CALLED BY ACCESS POINT: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/sensorstationdata/{id}")
    ArrayList<SensorStationDataFrame> getSensorStationData(@PathVariable("id") String id) {
        try {
            ArrayList<SensorStationDataFrame> returnList = sensorStationServiceApi.findSensorsByAccesspointID(Long.valueOf(id));
            sensorStationLogger.info("SENSOR STATION DATA CALLED BY ACCESS POINT: " + id);
            return returnList;
        }catch (SensorStationNotFoundException ex){
            sensorStationLogger.warning("SENSOR STATION DATA COULD NOT CALLED BY ACCESS POINT: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/api/validated/{id}")
    boolean getValidation(@PathVariable("id") String id) {
        try {
            boolean valid = sensorStationServiceApi.isValidated(Long.valueOf(id));
            sensorStationLogger.info("VALIDATION ASKED BY ACCESS POINT: " + id);
            return valid;
        }catch (AccessPointNotFoundException ex){
            sensorStationLogger.info("VALIDATION COULD NOT ASKED BY ACCESS POINT: " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }




}