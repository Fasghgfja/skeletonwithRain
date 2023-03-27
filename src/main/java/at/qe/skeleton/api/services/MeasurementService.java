package at.qe.skeleton.api.services;


import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;


@Service
public class MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;


    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, Measurement> measurements = new ConcurrentHashMap<>();

    /**
     public Measurement addMeasurement(Measurement measurement) {
     Measurement newMeasurement = new Measurement();
     newMeasurement.setId(ID_COUNTER.getAndIncrement());
     newMeasurement.setPlantID(measurement.getPlantID());
     newMeasurement.setUnit(measurement.getUnit());
     newMeasurement.setValue(measurement.getValue());
     newMeasurement.setType(measurement.getType());

     measurements.put(newMeasurement.getId(), newMeasurement);
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
     if (measurement.getPlantID() != null)

     measurements.computeIfPresent(id, (key, value) -> {
     value.setPlantID(measurement.getPlantID());
     return value;
     });

     if (measurement.getValue() != null)

     measurements.computeIfPresent(id, (key, value) -> {
     value.setValue(measurement.getValue());
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
     */





    public Collection<Measurement> getAllMeasurements(){
        return measurementRepository.findAll();
    }

    /**
     * The method returns a measurement by id.
     * @param id the id of measurement
     * @return the measurement with the given ID
     */
    public Measurement findMeasurementById(Long id){
        return measurementRepository.findFirstById(id);
    }

    /**
     * Retrieves all measurements from a plant.
     * @param plant the plant for which we filter the measurements
     * @return The measurements belonging to the selected plant as a Collection.
     */
    public Collection<Measurement> getAllMeasurementsByPlant(Plant plant){
        return measurementRepository.findMeasurementsByPlant(plant);
    }




    //--------------------Methods to find measurements by date.
    /**
     * Retrieves all measurements created before or equal to the given date as a collection.
     * @param date the date after which it filters, must not be {@literal null}
     * @return a Collection of all Measurements or an Empty one if no measurements have been found
     */
    public Collection<Measurement> getAllMeasurementsBefore(LocalDate date){
        return measurementRepository.findMeasurementsByTimestampLessThanEqual(date);
    }

    /**
     * Retrieves all measurements created after the given date as a collection.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsAfter(LocalDate date){
        return measurementRepository.findMeasurementsByTimestampGreaterThan(date);
    }

    /**
     * Retrieves all measurements created at a given date.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByDate(LocalDate date){
        return measurementRepository.findMeasurementsByTimestamp(date);
    }






    //Methods to find measurements by PlantId and date.
    /**
     * Retrieves all measurements from a plant created before or equal to the given date (the measurements)
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantBefore(Plant plant,LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTimestampLessThanEqual(plant,date);
    }

    /**
     * Retrieves all measurements from a plant created after the given date (the measurements)
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAfter(Plant plant,LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTimestampGreaterThan(plant,date);
    }

    /**
     * Retrieves all measurements created at a given date for a given plant.
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndDate(Plant plant,LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTimestamp(plant,date);
    }







    //Methods to find measurements by PlantId and type and date.
    /**
     * Retrieves all measurements of a specific type for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID ot type is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndType(Plant plant,String type){
        return measurementRepository.findMeasurementsByPlantAndType(plant,type);
    }

    /**
     * Retrieves all measurements of a specific type and date for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeAndDate(Plant plant,String type, LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestamp(plant,type,date);
    }

    /**
     * Retrieves all measurements of a specific type after a date for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeAfter(Plant plant,String type, LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestampGreaterThan(plant,type,date);
    }

    /**
     * Retrieves all measurements of a specific type before a date  for a given plant.
     * @param plant must not be {@literal null}.
     * @param type the type after which it filters, must not be {@literal null}
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plantID,type or date is {@literal null}.
     */
    public Collection<Measurement> getAllMeasurementsByPlantAndTypeBefore(Plant plant,String type, LocalDate date){
        return measurementRepository.findMeasurementsByPlantAndTypeAndTimestampLessThanEqual(plant,type,date);
    }




    //................methods to delete Measurements with di different parameters
    public void deleteMeasurement(Measurement measurement){
        if(measurement == null){
            throw new NullPointerException("Measurement cant be null");
        }
        measurementRepository.delete(measurement);
    }
    /**
     * The method removes the measurement with the given id from the database.
     * @param measurementId the id of the measurement
     * @throws NullPointerException if no measurement is found
     */
    public void deleteMeasurementById(Long measurementId){
        Measurement measurement = measurementRepository.findFirstById(measurementId);
        deleteMeasurement(measurement);
    }

    /**
     * Deletes all measurements for a given plant.
     * @param plant the plant from which to delete the measurements
     */
    public void deleteAllMeasuringPointsByPlant(Plant plant){
        Collection<Measurement> plantMeasurements = getAllMeasurementsByPlant(plant);
        for(Measurement m : plantMeasurements){
            deleteMeasurement(m);
        }
    }






    //................Methods to get measurements for a given plant in a interval of time..............
    /**
     * Returns the most recent measurments for a given plant (one per type).
     * @param plant for which the last measurement will be retrieved.
     * @return a list with one most recent measurement per type of the given plant
     */
    public Collection<Measurement> getLatestPlantMeasurements(Plant plant) {
        Collection<Measurement> latestMeasurements = getAllMeasurementsByPlant(plant);
        Comparator<Measurement> cmp = Comparator.comparing(Measurement::getTimestamp);
        List<Measurement> airHumidityMeasurements = new ArrayList<>(getAllMeasurementsByPlantAndType(plant,"HUMIDITY"));
        List<Measurement> temperatureMeasurements = new ArrayList<>(getAllMeasurementsByPlantAndType(plant,"TEMPERATURE"));
        List<Measurement> groundHumidityMeasurements = new ArrayList<>(getAllMeasurementsByPlantAndType(plant, "SOIL_MOISTURE"));
        List<Measurement> lightMeasurements = new ArrayList<>(getAllMeasurementsByPlantAndType(plant, "LIGHT_INTENSITY"));
        List<Measurement> airQualityMeasurements = new ArrayList<>(getAllMeasurementsByPlantAndType(plant, "AIR_QUALITY"));
        Measurement latestAirQulityMeasurement = airQualityMeasurements.stream().max(cmp).get();
        Measurement latestTemperatureMeasurement = temperatureMeasurements.stream().max(cmp).get();
        Measurement latestGroundHumidityMeasurement = groundHumidityMeasurements.stream().max(cmp).get();
        Measurement latestLightMeasurement = lightMeasurements.stream().max(cmp).get();
        Measurement latestAirHumidityMeasurement = airHumidityMeasurements.stream().max(cmp).get();
        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        return latestMeasurements;
    }


    //TODO remove this method and use the one above, we should search with plant and not sensor station most of the time
    public Collection<Measurement> getLatestPlantMeasurements(SensorStation sensorStation) {
        Collection<Measurement> latestMeasurements = new ArrayList<>();
        Comparator<Measurement> cmp = Comparator.comparing(Measurement::getTimestamp);
        List<Measurement> airHumidityMeasurements = new ArrayList<>(measurementRepository.findMeasurementsBySensorStationAndTypeLike(sensorStation,"HUMIDITY"));
        List<Measurement> temperatureMeasurements = new ArrayList<>(measurementRepository.findMeasurementsBySensorStationAndTypeLike(sensorStation,"TEMPERATURE"));
        List<Measurement> groundHumidityMeasurements = new ArrayList<>(measurementRepository.findMeasurementsBySensorStationAndTypeLike(sensorStation, "SOIL_MOISTURE"));
        List<Measurement> lightMeasurements = new ArrayList<>(measurementRepository.findMeasurementsBySensorStationAndTypeLike(sensorStation, "LIGHT_INTENSITY"));
        List<Measurement> airQualityMeasurements = new ArrayList<>(measurementRepository.findMeasurementsBySensorStationAndTypeLike(sensorStation, "AIR_QUALITY"));
        Measurement latestAirQulityMeasurement = airQualityMeasurements.stream().max(cmp).orElse(null);
        Measurement latestTemperatureMeasurement = temperatureMeasurements.stream().max(cmp).orElse(null);
        Measurement latestGroundHumidityMeasurement = groundHumidityMeasurements.stream().max(cmp).orElse(null);
        Measurement latestLightMeasurement = lightMeasurements.stream().max(cmp).orElse(null);
        Measurement latestAirHumidityMeasurement = airHumidityMeasurements.stream().max(cmp).orElse(null);
        latestMeasurements.add(latestAirQulityMeasurement);
        latestMeasurements.add(latestTemperatureMeasurement);
        latestMeasurements.add(latestGroundHumidityMeasurement);
        latestMeasurements.add(latestLightMeasurement);
        latestMeasurements.add(latestAirHumidityMeasurement);
        return latestMeasurements;
    }



    //public Collection<Measurement> getAllMeasuringPointsInIntervalByPlant(LocalDate start, LocalDate end, Plant plant){
    //    Collection<Measurement> measurements = getAllByPlant(plant);
    //   return measurements.stream().filter(x -> x.getTimestamp().after(start) && x.getTimestamp().before(end)).collect(Collectors.toList());
    //}
}
