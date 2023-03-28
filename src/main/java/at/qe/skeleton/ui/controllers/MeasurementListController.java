package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;


@Component
@Scope("view")
public class MeasurementListController implements Serializable {


        @Autowired
        private MeasurementService measurementService;

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




    }


