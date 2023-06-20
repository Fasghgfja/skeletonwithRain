package at.qe.skeleton.services;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.MeasurementType;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;




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

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AccessPointService.class);

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    private SensorStationRepository sensorStationRepository;

    private final Logger successLogger = Logger.getLogger("SuccessLogger");
    private FileHandler successFileHandler;

    /**
     * Method to get all measurements currently stored in the database.
     *
     * @return Collection of all measurements.
     */
    public Collection<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    /**
     * Method to get all Measurements of a given sensor station from the database.
     *
     * @param sensorStation measurements of this sensor station will be return in a way that the latest measurements are displayed first.
     * @return Collection of measurements.
     */
    public Collection<Measurement> getAllMeasurementsBySensorStation(SensorStation sensorStation) {
        return measurementRepository.findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation);
    }

    /**
     * Method to get measurements of a single type for a single sensor station.
     *
     * @param sensorStation Measurements for this sensor station will be returned.
     * @param type          Measurements of this type will be returned.
     * @return Collection of measurements.
     */
    public Collection<Measurement> getAllMeasurementsBySensorStationAndTypeAsc(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampAsc(sensorStation, type);
    }

    public Collection<Measurement> getAllMeasurementsBySensorStationAndType(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type);
    }


    /**
     * Method to get the latest measurements for a given sensorStation
     * this method is used from the graph and status indicators to get the latest measurements for a plant
     *
     * @param sensorStation the sensorStation from which to find the last measurements , 1 per type
     * @return a collection of the last measurements , one per type
     */
    public Collection<Measurement> getLatestPlantMeasurements(SensorStation sensorStation) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.HUMIDITY.getValue());
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.TEMPERATURE.getValue());
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.SOIL_MOISTURE.getValue());
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.LIGHT_INTENSITY.getValue());
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.AIR_QUALITY.getValue());
        Measurement latestPressureMeasurement = measurementRepository.findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(sensorStation, MeasurementType.AIR_PRESSURE.getValue());

        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        latestMeasurements.add(latestPressureMeasurement);
        return latestMeasurements;
    }

    /**
     * Method to get the latest measurements for a given plant
     * this method is used from the graph and status indicators to get the latest measurements for a plant
     *
     * @param plant the sensorStation from which to find the last measurements , 1 per type
     * @return a collection of the last measurements , one per type
     */
    public Collection<Measurement> getLatestPlantMeasurements(Plant plant) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.HUMIDITY.getValue());
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.TEMPERATURE.getValue());
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.SOIL_MOISTURE.getValue());
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.LIGHT_INTENSITY.getValue());
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.AIR_QUALITY.getValue());
        Measurement latestPressureMeasurement = measurementRepository.findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(plant, MeasurementType.AIR_PRESSURE.getValue());

        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        latestMeasurements.add(latestPressureMeasurement);

        return latestMeasurements;
    }

    /**
     * Method to get the amount of measurements currently stored in the database.
     *
     * @return number of measurements.
     */
    public Integer getMeasurementsAmount() {
        return measurementRepository.count();
    }


    /**
     * Method to return an icon for a certain measurement type.
     *
     * @param type type of the measurement.
     * @return A string that indicates which icon suits the measurement type is returned.
     */

    public String getMeasurementTypeIcon(String type) {
        return switch (type) {
            case "HUMIDITY" -> "fa-solid fa-droplet fa-lg";
            case "TEMPERATURE" -> "fa-solid fa-thermometer-three-quarters fa-lg";
            case "AIR_PRESSURE" -> "fa-sharp fa-solid fa-arrows-to-circle fa-lg";
            case "LIGHT_INTENSITY" -> "fa-solid fa-sun fa-lg";
            case "SOIL_MOISTURE" -> "fa-solid fa-water fa-lg";
            case "AIR_QUALITY" -> "fa-solid fa-wind fa-lg";
            default -> "";
        };
    }


    public String getLastMeasurementBySensorStationAndType(SensorStation sensorStation, String type) {
        Measurement measurement = measurementRepository.getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation, type);
        if (measurement == null) {
            return "--";
        }
        return measurementRepository.getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(sensorStation, type).getValue_s();
    }

    public void deleteMeasurementsFromTo(LocalDateTime from, LocalDateTime to) {
        if (from == null) {
            Measurement firstMeasurement = measurementRepository.findFirstByOrderByTimestampAsc();
            if (firstMeasurement == null) {
                return;
            }
            from = firstMeasurement.getTimestamp();
        }
        if (to == null) {
            to = LocalDateTime.now();
        }
        if (from != null && from.isAfter(to)) {
            return;
        }
        measurementRepository.deleteMeasurementsByTimestampBetween(from, to);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("ALL MEASUREMENTS DELETED FROM " + from + " TO " + to);
            successFileHandler.close();
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
    }

    public void deleteMeasurementsFromToForSensorStation(LocalDateTime from, LocalDateTime to, String sensorStationToDeleteFromId) {
        SensorStation sensorStation = sensorStationRepository.findFirstById(sensorStationToDeleteFromId);
        if (from == null) {
            Measurement firstMeasurement = measurementRepository.findFirstBySensorStationOrderByTimestampAsc(sensorStation);
            if (firstMeasurement == null) {
                return;
            }
            from = firstMeasurement.getTimestamp();

        }
        if (to == null) {
            to = LocalDateTime.now();
        }
        if (from != null && from.isAfter(to)) {
            return;
        }

        measurementRepository.deleteMeasurementsBySensorStationAndTimestampBetween(sensorStation, from, to);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("MEASUREMENTS FOR SENSOR STATION " + sensorStation.getSensorStationID() + " DELETED FROM " + from + " TO " + to);
            successFileHandler.close();
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
    }

    public List<Measurement> doGetMeasurementsByTypeAndSensorStationAndTimestampBetween(String chosenMeasurement, SensorStation sensorStation, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return measurementRepository.getMeasurementsByTypeAndSensorStationAndTimestampBetweenOrderByTimestampAsc(chosenMeasurement, sensorStation, dateFrom, dateTo);
    }

    public Measurement doFindFirstBySensorStationOrderByTimestampAsc(SensorStation sensorStation) {
        return measurementRepository.findFirstBySensorStationOrderByTimestampAsc(sensorStation);
    }

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public MeasurementRepository getMeasurementRepository() {
        return measurementRepository;
    }

    public void setSensorRepository(SensorRepository sensorRepository) {
    }

    public void setSensorStationRepository(SensorStationRepository sensorStationRepository) {
        this.sensorStationRepository = sensorStationRepository;
    }

    public SensorStationRepository getSensorStationRepository() {
        return sensorStationRepository;
    }

    public SensorRepository getSensorRepository() {
        return sensorRepository;
    }
}
