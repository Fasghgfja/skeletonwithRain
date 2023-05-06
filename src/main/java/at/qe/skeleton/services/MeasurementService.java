package at.qe.skeleton.services;

import java.util.*;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * Service for managing measurements entities {@link Measurement}.
 * ALL METHODS THAT RETURN MEASUREMENTS WILL DO SO ORDERED BY DATE ; MOST RECENT FIRST
 * Provides methods for loading, saving and removing Measurements
 * and adds methods to find measurements by measurementId  {@link MeasurementRepository#findFirstById(Long)},By Plant or Sensor Station,
 * along with a wide selection of methods to find measurements using different parameters:
 */
@Service
@Scope("application")
public class MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    SensorRepository sensorRepository;



    //................methods to find a Measurement or a list of measurements with different parameters
    public Measurement findMeasurementById(Long id) {
        return measurementRepository.findFirstById(id);
    }
    public Measurement findFirstMeasurementBySensorStationIdAndType(String sensorStationId,String type) {
        return measurementRepository.findFirstBySensorStationIdAndType(sensorStationId,type);
    }
    public Collection<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }
    public Collection<Measurement> getAllMeasurementsBySensorStation(SensorStation sensorStation) {
        return measurementRepository.findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation);
    }
    public Collection<Measurement> getAllMeasurementsBySensorStationAndType(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type);
    }





    //................methods to delete Measurements with di different parameters
    public void deleteMeasurement(Measurement measurement) {
        if (measurement == null) {
            throw new NullPointerException("Measurement cant be null");
        }
        measurementRepository.delete(measurement);
    }

    /**
     * Deletes all measurements for a given sensorStation.
     * @param sensorStation the sensorStation from which to delete the measurements
     */
    public void deleteMeasurementsBySensorStation(SensorStation sensorStation) {
        measurementRepository.deleteMeasurementsBySensorStation(sensorStation);
    }





    /**
     * Method to get the latest measurements for a given sensorStation
     * this method is used from the graph and status indicators to get the latest measurements for a plant
     * @param sensorStation the sensorStation from which to find the last measurements , 1 per type
     * @return a collection of the last measurements , one per type
     */
    public Collection<Measurement> getLatestPlantMeasurements(SensorStation sensorStation) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, "HUMIDITY");
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, "TEMPERATURE");
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, "SOIL_MOISTURE");
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, "LIGHT_INTENSITY");
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, "AIR_QUALITY");
        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        return latestMeasurements;
    }
    /**
     * Method to get the latest measurements for a given plant
     * this method is used from the graph and status indicators to get the latest measurements for a plant
     * @param plant the sensorStation from which to find the last measurements , 1 per type
     * @return a collection of the last measurements , one per type
     */
    public Collection<Measurement> getLatestPlantMeasurements(Plant plant) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, "HUMIDITY");
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, "TEMPERATURE");
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, "SOIL_MOISTURE");
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, "LIGHT_INTENSITY");
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, "AIR_QUALITY");
        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        return latestMeasurements;
    }

    public Integer getMeasurementsAmount() {return measurementRepository.count();}


    public String getMeasurementStatusForValue(String measurementValue,String type) {
        if (measurementValue == null || measurementValue.equals("")) {return "OK";}
        if (checkThreshold(measurementValue,type) == 0){return "OK";} else {return "Wrong";}
    }

    private int checkThreshold(String measurementValue, String type) {
        boolean isThresholdExceeded;
        switch(type) {
            case "SOIL_MOISTURE":
                isThresholdExceeded = (Double.parseDouble(measurementValue) > 95 || Double.parseDouble(measurementValue) < 10);
                return isThresholdExceeded ? 1 : 0;
            case "HUMIDITY":
                isThresholdExceeded = (Double.parseDouble(measurementValue) > 80 || Double.parseDouble(measurementValue) < 20);
                return isThresholdExceeded ? 1 : 0;
            case "AIR_PRESSURE":
                isThresholdExceeded = (Double.parseDouble(measurementValue) > 2 || Double.parseDouble(measurementValue) < 1);
                return isThresholdExceeded ? 1 : 0;
            case "TEMPERATURE":
                isThresholdExceeded = (Double.parseDouble(measurementValue) > 35 || Double.parseDouble(measurementValue) < 10);
                return isThresholdExceeded ? 1 : 0;
            case "AIR_QUALITY":
                isThresholdExceeded = (Double.parseDouble(measurementValue) < 50);
                return isThresholdExceeded ? 1 : 0;
            case "LIGHT_INTENSITY":
                isThresholdExceeded = (Double.parseDouble(measurementValue) > 1500 || Double.parseDouble(measurementValue) < 100);
                return isThresholdExceeded ? 1 : 0;
            default:
                return 0;
        }
    }


    public String getMeasurementTypeIcon(String type) {
        switch(type) {
            case "HUMIDITY":
                return "fa-solid fa-droplet fa-lg";
            case "TEMPERATURE":
                return "fa-solid fa-thermometer-three-quarters fa-lg";
            case "AIR_PRESSURE":
                return "fa-sharp fa-solid fa-arrows-to-circle fa-lg";
            case "LIGHT_INTENSITY":
                return "fa-solid fa-sun fa-lg";
            case "SOIL_MOISTURE":
                return "fa-solid fa-water fa-lg";
            case "AIR_QUALITY":
                return "fa-solid fa-wind fa-lg";
            default:
                return "";
        }
    }


















}