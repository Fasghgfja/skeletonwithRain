package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorApi;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.api.services.SensorStationServiceApi;
import at.qe.skeleton.model.SensorStation;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        System.out.println(sensorStationApi.toString());
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
    int createSensorApi(@RequestBody SensorApi sensorApi) {
        System.out.println(sensorApi.toString());
        try {
            return sensorStationServiceApi.addSensor(sensorApi);
        }catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/updatesensors")
    int updateSensorApi(@RequestBody SensorApi sensorApi) {
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
            System.out.println(id);
            return sensorStationServiceApi.findAllSensorStation(Long.valueOf(id));
        } catch (SensorStationNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/sensorsboardervalue")
    SensorApi updateMeasurement(@RequestBody SensorApi sensorApi) {
        try {
            return sensorStationServiceApi.findOneSensor(sensorApi.getSensor_id());
        }catch (SensorStationNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }




}