package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.*;
import at.qe.skeleton.api.services.SensorStationServiceApi;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@RestController
public class SensorStationApiController {


    @Autowired
    SensorStationServiceApi sensorStationServiceApi;

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
        } catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Response.SC_OK;
    }

    /**
     * This method is called to save new sensors
     * @param sensorApi
     * @return response ok
     */
    @PostMapping("/api/sensors")
    int createSensorApi(@RequestBody List<SensorApi> sensorApi) {
        for (SensorApi s:
             sensorApi) {
            System.out.println(s.toString());
        }

        try {
            return sensorStationServiceApi.addSensor(sensorApi);
        }catch (SensorNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/updatesensors")
    int updateSensorApi(@RequestBody List<SensorApi> sensorApi) {
        System.out.println(sensorApi.toString());
        try {
            sensorStationServiceApi.updateSensor(sensorApi);
            return Response.SC_OK;
        }catch (SensorStationNotFoundException ex){
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
            return sensorStationServiceApi.findAllSensorStation(Long.valueOf(id));
        } catch (SensorStationNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/sensorstationdata/{id}")
    ArrayList<SensorStationDataFrame> getSensorStationData(@PathVariable("id") String id) {
        try {
            return sensorStationServiceApi.findSensorsByAccesspointID(Long.valueOf(id));
        }catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/api/validated/{id}")
    boolean getValidation(@PathVariable("id") String id) {
        try {
            return sensorStationServiceApi.isValidated(Long.valueOf(id));
        }catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/api/auditLog")
    int logFile(@RequestBody List<LogFrame> logFrame) {

        try{
            sensorStationServiceApi.saveLog(logFrame);
            return Response.SC_OK;
        }catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}