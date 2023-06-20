package at.qe.skeleton.api.services;


import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement2;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.MeasurementType;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class MeasurementServiceApi {

    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    SensorStationService sensorStationService;

    private static final AtomicLong ID_COUNTER = new AtomicLong(1);

    //TODO: what is this ? why does it search from this hashmap and not the repository?
    private static final ConcurrentHashMap<Long, Measurement> measurements = new ConcurrentHashMap<>();
    public Measurement findOneMeasurement(Long id) throws MeasurementNotFoundException {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (measurementOptional.isPresent()) {
            return measurementOptional.get();
        } else {
            throw new MeasurementNotFoundException();
        }
    }



    /**
     * This method is called to store the reseived measurements into the database
     * @param measurement
     * @throws MeasurementNotFoundException
     */
    public void addMeasurement(List<Measurement2> measurement) throws MeasurementNotFoundException {
        for (Measurement2 m: measurement) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            DateTimeFormatter databaseFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(m.getTime_stamp(), formatter);
            Measurement measurement1 = new Measurement();
            SensorStation sensorStation = sensorStationService.loadSensorStation(m.getSensorStation());
            measurement1.setSensorStation(sensorStation);
            measurement1.setType(m.getType());
            measurement1.setTimestamp(dateTime);
            measurement1.setValue_s(m.getValue());
            measurement1.setUnit(getUnit(m.getType()));
            measurementRepository.save(measurement1);
        }

    }

    public String getUnit(String type){
        if(type.equals(MeasurementType.AIR_PRESSURE.getValue())){
            return "hpa";
        }
        else if (type.equals(MeasurementType.HUMIDITY.getValue()) || type.equals(MeasurementType.SOIL_MOISTURE.getValue())){
            return "%";
        }
        else if (type.equals(MeasurementType.TEMPERATURE.getValue())){
            return "°C";
        }
        else if (type.equals(MeasurementType.AIR_QUALITY.getValue())){
            return "ppm";
        }
        else {
            return "Lux";
        }

    }
}
