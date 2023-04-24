package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import java.time.LocalDate;
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



    //TODO: is it better to implement this in Measurement and search directly by plant?
    //    @ManyToOne
    //    @JoinColumn(name = "plantid", nullable = false)
    //    private Plant plant;
    // todo paired with the one in measurement
    /**
     * Retrieves all measurements from a plant.
     * @param plant must not be {@literal null}.
     * @return The measurements belonging to the selected plant as a List.
     * @throws IllegalArgumentException If plantID is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantOrderByTimestampDesc(Plant plant);


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
    List<Measurement> findMeasurementsBySensorStationAndTypeLikeOrderByTimestampDesc(SensorStation sensorStation, String type);








    //Methods to find measurements by date.
    /**
     * Retrieves all measurements created at a given date.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByTimestampOrderByTimestampDesc(LocalDate date);

    /**
     * Retrieves all measurements created before or equal to the given date.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If date is {@literal null}.
     */
    List<Measurement> findMeasurementsByTimestampLessThanEqualOrderByTimestampDesc(LocalDate date);

    /**
     * Retrieves all measurements created after the given date.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByTimestampGreaterThanOrderByTimestampDesc(LocalDate date);








    //Methods to find measurements by PlantId and date.
    /**
     * Retrieves all measurements created at a given date for a given plant.
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTimestampOrderByTimestampDesc(Plant plant, LocalDate date);

    /**
     * Retrieves all measurements from a plant created before or equal to the given date (the measurements)
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTimestampLessThanEqualOrderByTimestampDesc(Plant plant, LocalDate date);

    /**
     * Retrieves all measurements from a plant created after the given date (the measurements)
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTimestampGreaterThanOrderByTimestampDesc(Plant plant, LocalDate date);










    //Methods to find measurements by PlantId and type and date.
    /**
     * Retrieves all measurements of a specific type for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID ot type is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTypeLikeOrderByTimestampDesc(Plant plant, String type);

    /**
     * Retrieves all measurements of a specific type and date for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTypeAndTimestampOrderByTimestampDesc(Plant plant, String type, LocalDate date);

    /**
     * Retrieves all measurements of a specific type after a date for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTypeAndTimestampGreaterThanOrderByTimestampDesc(Plant plant, String type, LocalDate date);

    /**
     * Retrieves all measurements of a specific type before a date  for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    List<Measurement> findMeasurementsByPlantAndTypeAndTimestampLessThanEqualOrderByTimestampDesc(Plant plant, String type, LocalDate date);














    //new
    List<Measurement> findMeasurementsByPlantAndTypeOrderByTimestampDesc(Plant plant, String type);

    Measurement findFirstMeasurementByPlantAndTypeOrderByTimestampDesc(Plant plant, String type);





}


