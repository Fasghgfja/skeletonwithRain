package at.qe.skeleton.api.services;


import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement2;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class MeasurementServiceApi {

    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    SensorStationService sensorStationService;

    private static final AtomicLong ID_COUNTER = new AtomicLong(1); //TODO: what is this? do we need it? ..SH



    //TODO: what is this ? why does it search from this hashmap and not the repository?
    private static final ConcurrentHashMap<Long, Measurement> measurements = new ConcurrentHashMap<>();

    public Measurement findOneMeasurement(Long id) throws MeasurementNotFoundException {
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(id);
        if (optionalMeasurement.isPresent()) {
            return optionalMeasurement.get();
        } else {
            throw new MeasurementNotFoundException();
        }
    }






    public void addMeasurement(Measurement2 measurement) throws MeasurementNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(measurement.getTime_stamp(), formatter);

        Measurement measurement1 = new Measurement();
        SensorStation sensorStation = sensorStationService.loadSensorStation(measurement.getSensorStation());//TODO: new!
        measurement1.setSensorStation(sensorStation);
        measurement1.setType(measurement.getType());
        measurement1.setTimestamp(dateTime);
        measurement1.setValue_s(measurement.getValue());
        measurementRepository.save(measurement1);
        System.out.println(measurement1.toString());

    }





    public Measurement convertMeasurement(Measurement2 measurement) { //TODO: do we need this?
        Measurement newMeasurement = new Measurement();
        newMeasurement.setType(measurement.getSensor_id());
        newMeasurement.setValue_s(measurement.getValue());
        newMeasurement.setTimestamp(LocalDateTime.parse(measurement.getTime_stamp()));
        measurementRepository.save(newMeasurement);
        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);
        System.out.println(newMeasurement);
        newMeasurement = measurementRepository.findFirstById(newMeasurement.getId());
        System.out.println(newMeasurement);
        return newMeasurement;
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




}
