package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.api.services.SensorStationServiceApi;
import at.qe.skeleton.model.SensorStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class SensorStationApiController {


    @Autowired
    SensorStationServiceApi sensorStationServiceApi;

    @PostMapping("/api/sensorstations")
    SensorStationApi createSensorStationApi(@RequestBody SensorStationApi sensorStationApi) {
        System.out.println("test123");
        return sensorStationServiceApi.addSensorStation(sensorStationApi);
    }

    @GetMapping("/api/sensorstations/{id}")
    SensorStation getOneSensorStationApi(@PathVariable Long id) {
        try {
            return sensorStationServiceApi.findOneSensorStation(id);
        } catch (SensorStationNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
/*
    @PatchMapping("/api/sensorstations/{id}")
    Measurement updateMeasurement(@PathVariable long id, @RequestBody Measurement measurement) {
        return measurementService.updateMeasurement(id, measurement);
    }

 */


}