package at.qe.skeleton.api.controllers;

import at.qe.skeleton.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.services.MeasurementService;

@RestController
public class MeasurementController {
    /**



     @Autowired
     MeasurementService measurementService;

     @PostMapping("/api/measurements")
     Measurement createMeasurement(@RequestBody Measurement measurement)
     {
     return measurementService.addMeasurement(measurement);
     }

     @GetMapping("/api/measurements/{id}")
     Measurement getOneMeasurement(@PathVariable Long id)
     {
     try
     {
     return measurementService.findOneMeasurement(id);
     }
     catch(MeasurementNotFoundException ex)
     {
     throw new ResponseStatusException(HttpStatus.NOT_FOUND);
     }
     }

     @PatchMapping("/api/measurements/{id}")
     Measurement updateMeasurement(@PathVariable long id, @RequestBody Measurement measurement)
     {
     return measurementService.updateMeasurement(id, measurement);
     }
     */


}