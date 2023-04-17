package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.SensorStation;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.services.SensorStationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The class is responsible for handling Sensor Stations.
 */
@Getter
@Setter
@Component
@Scope("view")
public class SensorStationListController implements Serializable {

    @Autowired
    private SensorStationService sensorService;

    private Collection<SensorStation> filteredSensorStations;



    /**
     * Returns a list of all sensor stations.
     */
    public Collection<SensorStation> getSensorStations() {
        return sensorService.getAllSensorStations();
    }

    /**
     * Returns how many sensor stations are registered in the system.
     */
    public long getSensorStationsAmount() {
        return sensorService.getSensorStationsAmount();
    }

}
