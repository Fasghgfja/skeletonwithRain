package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementType;
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

    //TODO: implement this so that real measurments are sent
    /**
    @Autowired
    private MeasurementService measurementService;
     */


    /**
     * Attribute to cache the currently displayed sensor station.
     */
    private SensorStation sensorStation;

    /**
     * Mock for frontend , delete for real implementation.
     */
    private List<Measurement> latestMeasurements;


    /**
     * Mock for frontend , delete for real implementation.
     */
    public List<Measurement> getLatestMeasurements() {
        latestMeasurements = new ArrayList<>();
        Measurement airMeasurement = new Measurement();
        airMeasurement.setId(1L);
        airMeasurement.setType(MeasurementType.AIR_PRESSURE);
        airMeasurement.setUnit("bar");
        airMeasurement.setValue(1.0);
        airMeasurement.setPlantID(2L);
        Measurement tempMeasurement = new Measurement();
        tempMeasurement.setId(2L);
        tempMeasurement.setType(MeasurementType.TEMPERATURE);
        tempMeasurement.setUnit("°C");
        tempMeasurement.setValue(20.0);
        tempMeasurement.setPlantID(2L);
        Measurement humidityMeasurement = new Measurement();
        humidityMeasurement.setId(3L);
        humidityMeasurement.setType(MeasurementType.HUMIDITY);
        humidityMeasurement.setUnit("%");
        humidityMeasurement.setValue(50.0);
        humidityMeasurement.setPlantID(2L);
        Measurement lightMeasurement = new Measurement();
        lightMeasurement.setId(4L);
        lightMeasurement.setType(MeasurementType.LIGHT_INTENSITY);
        lightMeasurement.setUnit("lux");
        lightMeasurement.setValue(10.0);
        lightMeasurement.setPlantID(2L);
        latestMeasurements.add(airMeasurement);
        latestMeasurements.add(tempMeasurement);
        latestMeasurements.add(humidityMeasurement);
        latestMeasurements.add(lightMeasurement);
        return latestMeasurements;
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

    /**
     * Opens last measurement row toggle for selected sensor station.
     */
    //TODO: use the sensor station extracter from the event to make a call for its latest measurements
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            SensorStation sensorStation = (SensorStation) event.getData();
            if (latestMeasurements == null) {
                getLatestMeasurements();            }
        }
    }







}
