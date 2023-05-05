package at.qe.skeleton.api.services;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.api.model.Measurement2;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.PlantRepository;
import at.qe.skeleton.services.PlantService;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


/**
 * Service for managing measurements entities {@link Measurement}.
 * ALL METHODS THAT RETURN MEASUREMENTS WILL DO SO ORDERED BY DATE ; MOST RECENT FIRST
 * Provides methods for loading, saving and removing Measurements
 * and adds methods to find measurements by measurementId  {@link MeasurementRepository#findFirstById(Long)} and
 * By Plant {@link MeasurementRepository#findMeasurementsByPlantOrderByTimestampDesc(Plant)} .
 * among with a additional wide selection of methods to find measurements using different parameters:
 * The Derived Query are split into parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
@Service
public class MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    SensorStationService sensorStationService;
    @Autowired
    PlantService plantService;

    @Autowired
    PlantRepository plantRepository;
    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, Measurement> measurements = new ConcurrentHashMap<>();


    public void addMeasurement(Measurement2 measurement) throws MeasurementNotFoundException {
        System.out.println();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate dateTime = LocalDate.parse(measurement.getTime_stamp(), formatter);

        Measurement measurement1 = new Measurement();
        SensorStation sensorStation = sensorStationService.loadSensorStation(measurement.getSensorStation());//TODO: new!
        measurement1.setSensorStation(sensorStation);

        String type = measurement.getType();
        if (type.equals("TempSensor")) {measurement1.setType("SOIL_MOISTURE");}
        if (type.equals("LigthSensor")) {measurement1.setType("HUMIDITY");}
        if (type.equals("HygroSensor")) {measurement1.setType("AIR_PRESSURE");}
        if (type.equals("GasSensor")) {measurement1.setType("TEMPERATURE");}
        if (type.equals("humiditySensor")) {measurement1.setType("AIR_QUALITY");}
        if (type.equals("PressureSensor")) {measurement1.setType("LIGHT_INTENSITY");}
        measurement1.setTimestamp(dateTime);
        measurement1.setValue_s(measurement.getValue());
        measurementRepository.save(measurement1);
        System.out.println(measurement1.toString());

    }

    public Measurement convertMeasurement(Measurement2 measurement) { //TODO: do we need this?
        Measurement newMeasurement = new Measurement();
        newMeasurement.setType(measurement.getSensor_id());
        newMeasurement.setValue_s(measurement.getValue());
        newMeasurement.setTimestamp(LocalDate.parse(measurement.getTime_stamp()));
        measurementRepository.save(newMeasurement);
        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);
        System.out.println(newMeasurement);
        newMeasurement = measurementRepository.findFirstById(newMeasurement.getId());
        System.out.println(newMeasurement);
        return newMeasurement;
    }

    public Measurement findOneMeasurement(Long id) throws MeasurementNotFoundException {
        Measurement measurement = measurements.get(id);
        if (measurement != null)
            return measurement;
        else
            throw new MeasurementNotFoundException();
    }


    public Measurement updateMeasurement(long id, Measurement measurement) {
      //  if (measurement.getPlant() != null)//TODO: do we need this?what does this even do?
       //     measurements.computeIfPresent(id, (key, value) -> {
       //         value.setPlant(measurement.getPlant());
       //         return value;
       //     });

        if (measurement.getValue_s() != null)
            measurements.computeIfPresent(id, (key, value) -> {
                value.setValue_s(measurement.getValue_s());
                return value;
            });

        if (measurement.getUnit() != null)
            measurements.computeIfPresent(id, (key, value) -> {
                value.setUnit(measurement.getUnit());
                return value;
            });

        if (measurement.getType() != null)
            measurements.computeIfPresent(id, (key, value) -> {
                value.setType(measurement.getType());
                return value;
            });

        return measurements.get(id);

        //there are more elegant ways to do this: https://www.baeldung.com/spring-rest-json-patch
    }


    public Collection<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    /**
     * The method returns a measurement by id.
     *
     * @param id the id of measurement
     * @return the measurement with the given ID
     */
    public Measurement findMeasurementById(Long id) {
        return measurementRepository.findFirstById(id);
    }


    public Measurement findFirstMeasurementBySensorStationIdAndType(String sensorStationId,String type) {
        return measurementRepository.findFirstBySensorStationIdAndType(sensorStationId,type);
    }



    /**
     * Retrieves all measurements from a plant.
     *
     *  the plant for which we filter the measurements
     * @return The measurements belonging to the selected plant as a Collection.

    public Collection<Measurement> getAllMeasurementsByPlant(Plant plant) {
        return measurementRepository.findMeasurementsByPlantOrderByTimestampDesc(plant);
    }
     */

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
     * The method removes the measurement with the given id from the database.
     *
     * @param measurementId the id of the measurement
     * @throws NullPointerException if no measurement is found
     */
    public void deleteMeasurementById(Long measurementId) {
        Measurement measurement = measurementRepository.findFirstById(measurementId);
        deleteMeasurement(measurement);
    }


    /**
     * Deletes all measurements for a given sensorStation.
     *
     * @param sensorStation the sensorStation from which to delete the measurements
     */
    public void deleteMeasurementBySensorStation(SensorStation sensorStation) {
        Collection<Measurement> plantMeasurements = getAllMeasurementsBySensorStation(sensorStation);
        for (Measurement m : plantMeasurements) {
            deleteMeasurement(m);
        }
    }


    //................Methods to get all last measurements for a given plant ..............



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


    //TODO: push this down to repository and queue , it is too expensive here
    public Integer getMeasurementsAmount() {
        {
            return measurementRepository.findAll().stream().toList().size();
        }
    }


}
