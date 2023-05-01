package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collection;


@Service
@Scope("application")
public class SensorService {


    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private LogRepository logRepository;


    public Collection<Sensor> getAllSensor() {
        return sensorRepository.findAll();
    }

    //TODO: if this works implement all similar functions this way
    public Collection<Sensor> getAllSensorsBySensorStation(SensorStation station) {
        return sensorRepository.findSensorsBySensorStation(station);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public Sensor loadSensor(Long id) {
        return sensorRepository.findFirstById(id);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public Sensor saveSensor(Sensor sensor) {
        if (sensor.isNew()) {
            sensor.setCreateDate(LocalDate.now());
        } else {
            sensor.setUpdateDate(LocalDate.now());
        }
        return sensorRepository.save(sensor);
    }


    //TODO: implement logging , like it was it crashes the db
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensor(Sensor sensor) {
       // Log deleteLog = new Log();

       // deleteLog.setDate(LocalDate.now());
       // deleteLog.setSubject("SENSOR STATION DELETION");
       // deleteLog.setText("DELETED SENSOR STATION: " + sensor.getId());

      //  logRepository.save(deleteLog);
        sensorRepository.delete(sensor);
    }


    //TODO: push this down to repository and queue , it is too expensive here

    public long getSensorStationsAmount() {
        return sensorRepository.count();
    }

}
