package at.qe.skeleton.api.services;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
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
public class SensorStationServiceApi {

    @Autowired
    SensorStationRepository sensorStationRepository;


    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, SensorStation> sensorStations = new ConcurrentHashMap<>();


    public SensorStationApi addSensorStation(SensorStationApi sensorStation) {
        SensorStationApi newSensorStation = new SensorStationApi();
        newSensorStation.setName(sensorStation.getName());
        newSensorStation.setService_description(sensorStation.getService_description());
        newSensorStation.setAlarm_switch(sensorStation.getAlarm_switch());

        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);

        System.out.println(newSensorStation);
        SensorStation newSensorStation2 = convertMeasurement(newSensorStation);
        sensorStationRepository.save(newSensorStation2);
        newSensorStation2 = sensorStationRepository.findFirstById(newSensorStation.getName());

        return newSensorStation;
    }

    public SensorStation convertMeasurement(SensorStationApi sensorStationApi) {
        SensorStation newSensorStation = new SensorStation();
        newSensorStation.setSensorStationID(sensorStationApi.getName());
        newSensorStation.setLocation("No Location");
        //set alarm swith and implement it in java side
        sensorStationRepository.save(newSensorStation);
        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);
        System.out.println(newSensorStation);
        newSensorStation = sensorStationRepository.findFirstById(newSensorStation.getSensorStationName());
        System.out.println(newSensorStation);
        return newSensorStation;
    }

    public SensorStation findOneSensorStation(Long id) throws SensorStationNotFoundException {
        SensorStation sensorStation = sensorStations.get(id);
        if (sensorStation != null)
            return sensorStation;
        else
            throw new SensorStationNotFoundException();
    }



}
