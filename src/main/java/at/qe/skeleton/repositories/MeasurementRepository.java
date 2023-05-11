package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Repository for managing measurement entities {@link Measurement}.
 * ALL METHODS RETURN MEASUREMENTS ORDERED BY DATE ; MOST RECENT FIRST
 * Provides methods for loading, saving and removing Measurements as inherited from the Abstract Repository
 * and adds methods to find measurements by measurementId  {@link MeasurementRepository#findFirstById(Long)} and
 * By Plant {@link MeasurementRepository#findMeasurementsByPlantOrderByTimestampDesc(Plant)} .
 * among with a wide selection of methods to find measurements using different parameters:
 * The Derived Query are split into parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
public interface MeasurementRepository extends AbstractRepository<Measurement, Long> {


    /**
     * Retrieves a measurement entity by its id.
     * @param Id must not be {@literal null}.
     * @return The measurement with the given id.
     * @throws IllegalArgumentException If measurementID is {@literal null}.
     */
    Measurement findFirstById(Long Id);

    Measurement findFirstMeasurementBySensorStationAndTypeOrderByTimestampDesc(SensorStation sensorStation, String type);

    Measurement findFirstMeasurementBySensorStation_PlantAndTypeOrderByTimestampDesc(Plant plant, String type);



    /**
     * Retrieves all measurements from a sensor station.
     * @param sensorStation must not be {@literal null}.
     * @return The measurements belonging to the selected sensorStation as a List.
     * @throws IllegalArgumentException If sensorStation is {@literal null}.
     */
    List<Measurement> findMeasurementsBySensorStationOrderByTimestampDesc(SensorStation sensorStation);


    Measurement findFirstBySensorStationIdAndType(String sensorStationId,String type);


    /**
     * Retrieves all measurements from a sensor station of a given type.
     * @param sensorStation must not be {@literal null}.
     * @param type must not be {@literal null}.
     * @return The measurements belonging to the selected sensorStation as a List.
     * @throws IllegalArgumentException If sensorStation is {@literal null}.
     */
    List<Measurement> findMeasurementsBySensorStationAndTypeLikeOrderByTimestampAsc(SensorStation sensorStation, String type);
    List<Measurement> findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(SensorStation sensorStation, String type);


    void deleteMeasurementsBySensorStation(SensorStation sensorStation);

    Integer count();


    Measurement getFirstBySensorStationAndTypeEqualsOrderByTimestampDesc(SensorStation sensorStation, String type);

    @Transactional
    void deleteMeasurementsByTimestampBetween(LocalDateTime from, LocalDateTime to);

    Measurement findFirstByOrderByTimestampAsc();

    Measurement findFirstByOrderByTimestampDesc();
}


