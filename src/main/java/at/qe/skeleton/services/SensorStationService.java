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
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;


@Service
@Scope("application")
public class SensorStationService {


    @Autowired
    private SensorStationRepository sensorStationRepository;

    @Autowired
    private UserxRepository userRepository;


    @Autowired
    private LogRepository logRepository;


    public Collection<SensorStation> getAllSensorStations() {
        return sensorStationRepository.findAll();
    }


    public Collection<SensorStation> getAllAssignedSensorStations(Userx user) {
        return sensorStationRepository.findSensorStationsByGardener(user);
    }

    public void addGardenerToSensorStation(SensorStation sensorStation, String user) {//TODO:New , test this
        if(user == null || sensorStation == null  ) {return;}//need to remove lazy for this , check if its doable speedwise userloa || sensorStation.getGardener().contains(user)
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        Userx userload = userRepository.findFirstByUsername(user);

        userload.getSensorStationsUnderCare().add(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
       // sensorStationRepository.save(sensorStation);// not needed i think
        userRepository.save(userload);
    }

    public void removeGardenerFromSensorStation(SensorStation sensorStation, Userx user) {//TODO:New , Test this
        if(user == null || sensorStation == null  ) {return;}//need to remove lazy for this , check if its doable speedwise userloa || sensorStation.getGardener().contains(user)
        Userx userload = userRepository.findFirstByUsername(user.getId());
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        userload.getSensorStationsUnderCare().remove(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
        userRepository.save(userload);
       // sensorStationRepository.save(sensorStation);// not needed i think
    }





    @PreAuthorize("hasAuthority('ADMIN')")
    public SensorStation loadSensorStation(String id) {
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


    public long getSensorStationsAmount() {
        return sensorStationRepository.count();
    }


    public Collection<String> getAllSensorStationsIds() {//this is used to return a list of sensorstations ids to delete from when deleting measurements
        return sensorStationRepository.getAllSensorStationsIds();
    }
}
