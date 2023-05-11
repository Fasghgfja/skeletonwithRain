package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Component
@Scope("view")
public class MeasurementListController implements Serializable {

    private List<Measurement> measurementsForSensorStationList;


    @Autowired
    private transient MeasurementService measurementService;

    @Autowired
    private transient SensorService sensorService;

    @Autowired
    private SensorStationDetailController sensorStationDetailController;

    @Autowired
    private GraphController graphController;


    @PostConstruct
    public void init(){
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        if (type == null || type.equals("all")) {
            measurementsForSensorStationList = (ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStation(sensorStation);
        } else measurementsForSensorStationList = (ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, type);
    }

     /**This retieves the status of the alarm of a sensor of a sensor station
      * it will be Ok if alarm count is less then a fix number , and wronng if alarm count too high*/
    public String getSensorStatus(String type, SensorStation sensorStation) {
        return sensorService.getSensorStatus(type,sensorStation);
    }


    public String getMeasurementTypeIcon(String type) {
        return measurementService.getMeasurementTypeIcon(type);
    }



    public void reSetType() {
        this.type = "all";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStation(sensorStation));
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
            setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStation(sensorStation));
            return measurementService.getAllMeasurementsBySensorStation(sensorStation);
        } else{
            setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
            return measurementService.getAllMeasurementsBySensorStationAndType(sensorStation, type);
        }
    }




    public void onAirQualityClick() {//todo:move them all to graph controller and so
        type = "AIR_QUALITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("AIR_QUALITY",sensorStation);
    }
    public void onTempClick() {
        type = "TEMPERATURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("TEMPERATURE",sensorStation);
    }
    public void onAirHumidityClick() {
        type = "HUMIDITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("HUMIDITY",sensorStation);
    }
    public void onGroundHumidityClick() {
        type = "SOIL_MOISTURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("SOIL_MOISTURE",sensorStation);
    }
    public void onAirPressureClick() {
        type = "AIR_PRESSURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("AIR_PRESSURE",sensorStation);
    }
    public void onLightIntensityClick() {
        type = "LIGHT_INTENSITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        setMeasurementsForSensorStationList((ArrayList<Measurement>) measurementService.getAllMeasurementsBySensorStationAndType(sensorStation,type));
        graphController.selectLineGraph("LIGHT_INTENSITY",sensorStation);
    }






    public String getLastAirMeasurementValue() {
        type = "AIR_QUALITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }
    public String getLastPressureMeasurementValue() {
        type = "AIR_PRESSURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }
    public String getLastLightMeasurementValue() {
        type = "LIGHT_INTENSITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }
    public String getLastSoilMeasurementValue() {
        type = "SOIL_MOISTURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }
    public String getLastAirHumidityMeasurementValue() {
        type = "HUMIDITY";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }
    public String getLastTemperatureMeasurementValue() {
        type = "TEMPERATURE";
        SensorStation sensorStation = sensorStationDetailController.getSensorStation();
        return measurementService.getLastMeasurementBySensorStationAndType(sensorStation,type);
    }







}


