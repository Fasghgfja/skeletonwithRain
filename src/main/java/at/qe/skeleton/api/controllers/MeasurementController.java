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
import java.util.logging.Logger;

@RestController
public class MeasurementController {


    @Autowired
    MeasurementServiceApi measurementServiceApi;
    private final transient Logger measurementLogger = Logger.getLogger("Rest Api Measurement Logger");
    @PostMapping("/api/measurements")
    int createMeasurement(@RequestBody List<Measurement2> measurement2) {
        try{
            measurementServiceApi.addMeasurement(measurement2);
            String name = "";
            for (Measurement2 m2:
                 measurement2) {
                if(!name.equals(m2.getSensorStation())){
                    name = m2.getSensorStation();
                    measurementLogger.info("MEASUREMENTS ADDED OF SENSOR STATION: " + m2.getSensorStation());
                }
            }
            return Response.SC_OK;
        }catch (MeasurementNotFoundException ex){
            measurementLogger.warning("Exception caught by POST request createMeasurement: " + ex.toString());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
// is not used by Hardware can be deleted
    @GetMapping("/api/measurements/{id}")
    Measurement getOneMeasurement(@PathVariable Long id) {
        try {
            return measurementServiceApi.findOneMeasurement(id);
        } catch (MeasurementNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}