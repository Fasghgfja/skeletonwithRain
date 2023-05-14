package at.qe.skeleton.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.AbstractRepository;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    private SensorStationRepository sensorStationRepository;


    //................methods to find a Measurement or a list of measurements with different parameters
    // TODO: Methoden werden nicht verwendet. Bitte löschen. NICHT GETESTET.
    public Measurement findMeasurementById(Long id) {
        return measurementRepository.findFirstById(id);
    }
    public Measurement findFirstMeasurementBySensorStationIdAndType(String sensorStationId,String type) {
        return measurementRepository.findFirstBySensorStationIdAndType(sensorStationId,type);
    }

    /**
     * Method to get all measurements currently stored in the database.
     * @return Collection of all measurements.
     */
    public Collection<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    /**
     * Method to get all Measurements of a given sensor station from the database.
     * @param sensorStation measurements of this sensor station will be return in a way that the latest measurements are displayed first.
     * @return Collection of measurements.
     */
    public Collection<Measurement> getAllMeasurementsBySensorStation(SensorStation sensorStation) {
        return measurementRepository.findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation);
    }

    /**
     * Method to get measurements of a single type for a single sensor station.
     * @param sensorStation Measurements for this sensor station will be returned.
     * @param type Measurements of this type will be returned.
     * @return Collection of measurements.
     */
    public Collection<Measurement> getAllMeasurementsBySensorStationAndTypeAsc(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampAsc(sensorStation, type);
    }

    public Collection<Measurement> getAllMeasurementsBySensorStationAndType(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type);
    }





    //................methods to delete Measurements with di different parameters
    // TODO: THIS WILL BE NEEDED
    public void deleteMeasurement(Measurement measurement) {
        if (measurement == null) {
            throw new NullPointerException("Measurement cant be null");
        }
        measurementRepository.delete(measurement);
    }

    //TODO: THIS WILL BE NEEDED
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

    /**
     * Method to get the amount of measurements currently stored in the database.
     * @return number of measurements.
     */
    public Integer getMeasurementsAmount() {return measurementRepository.count();}



    /**
     * Method to return an icon for a certain measurement type.
     * @param type type of the measurement.
     * @return A string that indicates which icon suits the measurement type is returned.
     */

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


    public String getLastMeasurementBySensorStationAndType(SensorStation sensorStation, String type) {
        Measurement measurement = measurementRepository.getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation,type);
        if(measurement == null) {return "--";}
        return measurementRepository.getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation,type).getValue_s();
    }

    public void deleteMeasurementsFromTo(LocalDateTime from, LocalDateTime to) {
        if (from == null){
            Measurement firstMeasurement = measurementRepository.findFirstByOrderByTimestampAsc();
            if (firstMeasurement == null) {return;}
            from = firstMeasurement.getTimestamp();
        }
        if (to == null){
            to = LocalDateTime.now();
        }
        if ( from != null && from.isAfter(to)){return;}
        measurementRepository.deleteMeasurementsByTimestampBetween(from,to);
    }

    public void deleteMeasurementsFromToForSensorStation(LocalDateTime from, LocalDateTime to, String sensorStationToDeleteFromId) {
        SensorStation sensorStation = sensorStationRepository.findFirstById(sensorStationToDeleteFromId);
        if (from == null) {
            Measurement firstMeasurement = measurementRepository.findFirstBySensorStationOrderByTimestampAsc(sensorStation);
            if (firstMeasurement == null) {return;}
            from = firstMeasurement.getTimestamp();

        }
        if (to == null) {
            to = LocalDateTime.now();
        }
        if (from != null&& from.isAfter(to)){return;}

        measurementRepository.deleteMeasurementsBySensorStationAndTimestampBetween(sensorStation, from, to);
    }

}
