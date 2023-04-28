package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorApi;
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

import java.util.List;


@RestController
public class SensorStationApiController {


    @Autowired
    SensorStationServiceApi sensorStationServiceApi;

    @PostMapping("/api/sensorstations")
    SensorStationApi createSensorStationApi(@RequestBody SensorStationApi sensorStationApi) {
        System.out.println(sensorStationApi.toString());
        //TODO take object sensorStationApi and call:
        // if(decription == null){update SensorStation set description=sensorStationApi.getDescription() where name=sensorStationApi.getName();}
        // else {update SensorStation set alarm_count=sensorStationApi.getAlarmCount() where name=name=sensorStationApi.getName();}
        return sensorStationServiceApi.addSensorStation(sensorStationApi);
    }

    @PostMapping("/api/sensors")
    SensorApi createSensorApi(@RequestBody SensorApi sensorApi) {
        System.out.println(sensorApi.toString());
        return sensorStationServiceApi.addSensor(sensorApi);
    }

    @GetMapping("/api/getsensorstations")
    SensorStation getOneSensorStationApi(@RequestBody SensorStationApi sensorStationApi) {
        try {
            return sensorStationServiceApi.findOneSensorStation(sensorStationApi.getName());//.getAlarmSwitch()
        } catch (SensorStationNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    //Added to call all sensorstations via rest
    @GetMapping("/api/sensorstations")
    List<SensorStation> getAllSensorStationApi() {
        try {
            return sensorStationServiceApi.findAllSensorStation();
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