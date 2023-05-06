package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Collection;


@Component
@Scope("view")
public class MeasurementListController implements Serializable {


    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorStationDetailController sensorStationDetailController;

     /**This retieves the status of the alarm of a sensor of a sensor station
      * it will be Ok if alarm count is less then a fix number , and wronng if alarm count too high*/
    public String getSensorStatus(String type, SensorStation sensorStation) {//TODO:.. new...
        return sensorService.getSensorStatus(type,sensorStation);
    }

    public String getMeasurementStatusForValue(String measurementValue,String type) {
        return measurementService.getMeasurementStatusForValue(measurementValue,type);
    }

    public String getMeasurementTypeIcon(String type) {
        return measurementService.getMeasurementTypeIcon(type);
    }



    public void reSetType() {
        this.type = "all";
    }

    private String type = "all";


    /**
     * Returns a list of all measurements.
     */
    public Collection<Measurement> getMeasurements() {
        return measurementService.getAllMeasurements();
    }


    /**
     * Returns how many sensor stations are registered in the system.
     */

    public Integer getMeasurementsAmount() {
        return measurementService.getMeasurementsAmount();
    }


    public Collection<Measurement> getMeasurementsForSensorStation() {
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        if (type == null || type.equals("all")) {
            return measurementService.getAllMeasurementsBySensorStation(sensorStation);
        } else return measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, type);
    }

    public void onAirQualityClick() {
        type = "AIR_QUALITY";
    }

    public void onTempClick() {
        type = "TEMPERATURE";
    }

    public void onAirHumidityClick() {
        type = "HUMIDITY";
    }

    public void onGroundHumidityClick() {
        type = "SOIL_MOISTURE";
    }

    public void onAirPressureClick() {
        type = "AIR_PRESSURE";
    }

    public void onLightIntensityClick() {
        type = "LIGHT_INTENSITY";
    }







}


