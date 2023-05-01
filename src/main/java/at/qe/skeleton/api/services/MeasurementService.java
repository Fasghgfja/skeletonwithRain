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
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.core.parameters.P;
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


        if (sensorStation.getPlant() != null) { //TODO: new! test this
            measurement1.setPlant(plantRepository.findFirstByPlantName(sensorStation.getPlant().getPlantName()));
            //measurement1.setType(measurement.getType());
        }//TODO: new!

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

        /*
        Measurement2 newMeasurement = new Measurement2();
        newMeasurement.setSensorStation(measurement.getSensorStation());
        newMeasurement.setSensor_id(measurement.getSensor_id());
        newMeasurement.setValue(measurement.getValue());
        newMeasurement.setTime_stamp(measurement.getTime_stamp());

        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);

        System.out.println(newMeasurement);
        Measurement newMeasurement2 = convertMeasurement(newMeasurement);
        measurementRepository.save(newMeasurement2);
        newMeasurement2 = measurementRepository.findFirstById(newMeasurement2.getId());

        return newMeasurement;

         */
    }

    public Measurement convertMeasurement(Measurement2 measurement) {
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
        if (measurement.getPlant() != null)
            measurements.computeIfPresent(id, (key, value) -> {
                value.setPlant(measurement.getPlant());
                return value;
            });

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
     * @param plant the plant for which we filter the measurements
     * @return The measurements belonging to the selected plant as a Collection.
     */
    public Collection<Measurement> getAllMeasurementsByPlant(Plant plant) {
        return measurementRepository.findMeasurementsByPlantOrderByTimestampDesc(plant);
    }


    public Collection<Measurement> getAllMeasurementsBySensorStation(SensorStation sensorStation) {
        return measurementRepository.findMeasurementsBySensorStationOrderByTimestampDesc(sensorStation);
    }

    public Collection<Measurement> getAllMeasurementsBySensorStationAndType(SensorStation sensorStation, String type) {
        return measurementRepository.findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(sensorStation, type);
    }


    //--------------------Methods to find measurements by date.

    /**
     * Retrieves all measurements created before or equal to the given date as a collection.
     *
     * @param date the date after which it filters, must not be {@literal null}
     * @return a Collection of all Measurements or an Empty one if no measurements have been found
     */
    public Collection<Measurement> getAllMeasurementsBefore(LocalDate date) {
        return measurementRepository.findMeasurementsByTimestampLessThanEqualOrderByTimestampDesc(date);
    }

    /**
     * Retrieves all measurements created after the given date as a collection.
     *
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsAfter(LocalDate date) {
        return measurementRepository.findMeasurementsByTimestampGreaterThanOrderByTimestampDesc(date);
    }

    /**
     * Retrieves all measurements created at a given date.
     *
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByDate(LocalDate date) {
        return measurementRepository.findMeasurementsByTimestampOrderByTimestampDesc(date);
    }


    //Methods to find measurements by PlantId and date.

    /**
     * Retrieves all measurements from a plant created before or equal to the given date (the measurements)
     *
     * @param plant must not be {@literal null}.
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantBefore(Plant plant, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTimestampLessThanEqualOrderByTimestampDesc(plant, date);
    }

    /**
     * Retrieves all measurements from a plant created after the given date (the measurements)
     *
     * @param plant must not be {@literal null}.
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAfter(Plant plant, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTimestampGreaterThanOrderByTimestampDesc(plant, date);
    }

    /**
     * Retrieves all measurements created at a given date for a given plant.
     *
     * @param plant must not be {@literal null}.
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndDate(Plant plant, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTimestampOrderByTimestampDesc(plant, date);
    }


    //Methods to find measurements by PlantId and type and date.

    /**
     * Retrieves all measurements of a specific type for a given plant.
     *
     * @param plant must not be {@literal null}.
     * @param type  the type after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID ot type is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndType(Plant plant, String type) {
        return measurementRepository.findMeasurementsByPlantAndTypeLikeOrderByTimestampDesc(plant, type);
    }

    /**
     * Retrieves all measurements of a specific type and date for a given plant.
     *
     * @param plant must not be {@literal null}.
     * @param type  the type after which it filters, must not be {@literal null}
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeAndDate(Plant plant, String type, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestampOrderByTimestampDesc(plant, type, date);
    }

    /**
     * Retrieves all measurements of a specific type after a date for a given plant.
     *
     * @param plant must not be {@literal null}.
     * @param type  the type after which it filters, must not be {@literal null}
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeAfter(Plant plant, String type, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestampGreaterThanOrderByTimestampDesc(plant, type, date);
    }

    /**
     * Retrieves all measurements of a specific type before a date  for a given plant.
     *
     * @param plant must not be {@literal null}.
     * @param type  the type after which it filters, must not be {@literal null}
     * @param date  the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeBefore(Plant plant, String type, LocalDate date) {
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestampLessThanEqualOrderByTimestampDesc(plant, type, date);
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
     * Deletes all measurements for a given plant.
     *
     * @param plant the plant from which to delete the measurements
     */
    public void deleteMeasurementByPlant(Plant plant) {
        Collection<Measurement> plantMeasurements = getAllMeasurementsByPlant(plant);
        for (Measurement m : plantMeasurements) {
            deleteMeasurement(m);
        }
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

    /**
     * Returns the most recent measurments for a given plant (one per type).
     *
     * @param plant for which the last measurement will be retrieved.
     * @return a list with one most recent measurement per type of the given plant
     */
    public Collection<Measurement> getLatestPlantMeasurements(Plant plant) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(plant, "HUMIDITY");
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(plant, "TEMPERATURE");
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(plant, "SOIL_MOISTURE");
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(plant, "LIGHT_INTENSITY");
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(plant, "AIR_QUALITY");
        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        return latestMeasurements;
    }

    public Collection<Measurement> getLatestPlantMeasurements(SensorStation sensorStation) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Measurement latestAirQulityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(sensorStation.getPlant(), "HUMIDITY");
        Measurement latestTemperatureMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(sensorStation.getPlant(), "TEMPERATURE");
        Measurement latestGroundHumidityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(sensorStation.getPlant(), "SOIL_MOISTURE");
        Measurement latestLightMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(sensorStation.getPlant(), "LIGHT_INTENSITY");
        Measurement latestAirHumidityMeasurement = measurementRepository.findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(sensorStation.getPlant(), "AIR_QUALITY");
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
