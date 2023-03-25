package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
@Service
@Scope("application")
public class SensorStationService {

    @Autowired
    private SensorStationRepository sensorStationRepository;
    @Autowired
    private LogRepository logRepository;
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<SensorStation> getAllSensorStation() {
        return sensorStationRepository.findAll();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation loadSensorStation(Long id) {
        return sensorStationRepository.findFirstById(id);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation saveSensorStation(SensorStation sensorStation) {
        if (sensorStation.isNew()) {
            sensorStation.setCreateDate(LocalDate.now());
        } else {
            sensorStation.setUpdateDate(LocalDate.now());
        }
        return sensorStationRepository.save(sensorStation);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensorStation(SensorStation sensorStation) {
        Log deleteLog = new Log();

        deleteLog.setDate(LocalDate.now());
        deleteLog.setSubject("SENSOR STATION DELETION");
        deleteLog.setText("DELETED SENSOR STATION: " + sensorStation.getSensorStationID());

        logRepository.save(deleteLog);
        sensorStationRepository.delete(sensorStation);
    }

}