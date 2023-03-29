package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Collection;


@Component
@Scope("view")
public class MeasurementListController implements Serializable {


        /**
        * Autowired dependencies.
        * Spring will automatically resolve and inject a matching bean from the Spring application context at runtime.
        */
        @Autowired
        private MeasurementService measurementService;
        @Autowired
        private SensorStationDetailController sensorStationDetailController;


        /**
         * Returns a list of all measurements.
         */
        public Collection<Measurement> getMeasurements() {
            return measurementService.getAllMeasurements();
        }

        /**
         * Returns how many sensor stations are registered in the system.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        public Integer getMeasurementsAmount() {
            return measurementService.getMeasurementsAmount();
        }


          public Collection<Measurement> getMeasurementsForSensorStation() {
            SensorStation sensorStation = sensorStationDetailController.getSensorStation();
            return measurementService.getAllMeasurementsBySensorStation(sensorStation);
        }

}


