package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.SensorStation;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The class is responsible for handling Sensor Stations.
 */
@Component
@Scope("view")
public class SensorStationListController implements Serializable {

    @Autowired
    private SensorStationService sensorService;

    /**
     * Returns a list of all sensor stations.
     */
    public Collection<SensorStation> getSensorStations() {
        return sensorService.getAllSensorStations();
    }

    /**
     * Returns how many sensor stations are registered in the system.
     */
    public Integer getSensorStationsAmount() {
        return sensorService.getSensorStationsAmount();
    }

}
