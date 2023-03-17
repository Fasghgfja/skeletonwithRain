package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement;
import at.qe.skeleton.api.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MeasurementController {
    @Autowired
    MeasurementService measurementService;

    @PostMapping("/api/measurements")
    private Measurement createMeasurement(@RequestBody Measurement measurement){
        return measurementService.addMeasurement(measurement);
    }

    @GetMapping("/api/measurements/{id}")
    private Measurement getOneMeasurement(@PathVariable Long id){
        try{
            return measurementService.findOneMeasurement(id);
        } catch(MeasurementNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
