package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.SensorStation;
import java.io.Serializable;

import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the sensor stations detail view.
 */
@Component
@Scope("view")
public class SensorStationDetailController implements Serializable {

    @Autowired
    private SensorStationService sensorService;

    /**
     * Attribute to cache the currently displayed user
     */
    private SensorStation sensorStation;

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadSensorStation()}, {@link #doSaveSensorStation()} and
     * {@link #doDeleteSensorStation()}.
     *
     * @param sensorStation
     */
    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
        doReloadSensorStation();
    }

    /**
     * Returns the currently displayed Sensor Station.
     * @return currently displayed Sensor Station.
     */
    public SensorStation getSensorStation() {
        return sensorStation;
    }

    /**
     * Action to force a reload of the currently displayed Sensor Station.
     */
    public void doReloadSensorStation() {
        sensorStation = sensorService.loadSensorStation(sensorStation.getId());
    }

    /**
     * Action to save the currently displayed Sensor Station.
     */
    public void doSaveSensorStation() {
        sensorStation = this.sensorService.saveSensorStation(sensorStation);
    }

    /**
     * Action to delete the currently displayed Sensor Station.
     */
    public void doDeleteSensorStation() {
        this.sensorService.deleteSensorStation(sensorStation);
        sensorStation = null;
    }



}
