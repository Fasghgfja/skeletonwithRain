package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.services.MeasurementServiceApi;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.api.model.Measurement2;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;

import java.util.List;


//TODO: this class is to be renamed to MeasurementControllerApi but it should be done with Sh as it probably need changes in the hw src aswell
@RestController
public class MeasurementController {


    @Autowired
    MeasurementServiceApi measurementServiceApi;

    @PostMapping("/api/measurements")
    int createMeasurement(@RequestBody List<Measurement2> measurement2) {
        System.out.println("test123");
        try{
            measurementServiceApi.addMeasurement(measurement2);


            return Response.SC_OK;
        }catch (MeasurementNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/api/measurements/{id}")
    Measurement getOneMeasurement(@PathVariable Long id) {
        try {
            return measurementServiceApi.findOneMeasurement(id);
        } catch (MeasurementNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
/*
    @PatchMapping("/api/measurements/{id}")
    Measurement updateMeasurement(@PathVariable long id, @RequestBody Measurement measurement) {
        return measurementService.updateMeasurement(id, measurement);
    }

 */


}