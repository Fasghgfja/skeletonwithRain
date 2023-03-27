package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.qe.skeleton.services.SensorStationService;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
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


    @Autowired
    private MeasurementService measurementService;


    @Autowired
    private GraphController GraphController;


    /**
     * Attribute to cache the currently displayed sensor station.
     */
    private SensorStation sensorStation;

    /**
     * Attribute to cache the latestMEasurements.
     */
    private List<Measurement> latestMeasurements;

    public List<Measurement> getLatestMeasurements() {
        latestMeasurements = new ArrayList<>(measurementService.getLatestPlantMeasurements(sensorStation));
        return latestMeasurements;
    }

    /**
     * Opens last measurement row toggle for selected sensor station.
     */
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            sensorStation = (SensorStation) event.getData();
            if (sensorStation != this.sensorStation){
                getLatestMeasurements();            }
        }
    }




    /**
     * Sets the currently displayed sensor station and reloads it form db. This sensor station is
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
