package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.*;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@Scope("application")
public class SensorStationService {


    @Autowired
    private SensorStationRepository sensorStationRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private UserxRepository userRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private AccessPointRepository accessPointRepository;


    @Autowired
    private LogRepository logRepository;


    /**
     * Method to get all sensor stations currently stored in the database.
     * @return Collection of all sensor stations.
     */
    public Collection<SensorStation> getAllSensorStations() {
        return sensorStationRepository.findAll();
    }


    public Collection<SensorStation> getAllAssignedSensorStations(Userx user) {
        return sensorStationRepository.findSensorStationsByGardener(user);
    }

    public void addGardenerToSensorStation(SensorStation sensorStation, String user) {
        if(user == null || sensorStation == null  ) {return;}//need to remove lazy for this , check if its doable speedwise userloa || sensorStation.getGardener().contains(user)
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        Userx userload = userRepository.findFirstByUsername(user);

        userload.getSensorStationsUnderCare().add(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
       // sensorStationRepository.save(sensorStation);// not needed i think
        userRepository.save(userload);
    }

    public void removeGardenerFromSensorStation(SensorStation sensorStation, Userx user) {
        if(user == null || sensorStation == null  ) {return;}//need to remove lazy for this , check if its doable speedwise userloa || sensorStation.getGardener().contains(user)
        Userx userload = userRepository.findFirstByUsername(user.getId());
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        userload.getSensorStationsUnderCare().remove(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
        userRepository.save(userload);
       // sensorStationRepository.save(sensorStation);// not needed i think
    }



    public void removeAccessPointFromSensorStations(AccessPoint accessPoint) {
        if(accessPoint == null) {return;}
        AccessPoint accessLoad = accessPointRepository.findFirstById(accessPoint.getId());
        sensorStationRepository.setAccessPointToNull(accessLoad);
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


    @PreAuthorize("hasAuthority('ADMIN')") //TODO: new! test this
    // TODO:add proper logging maybe with more info on what happened during the process or eventual exception messages, this can fail!
    public void deleteSensorStation(SensorStation sensorStation) {
        Log deleteLog = new Log();

        //delete measurements for sensor station
        measurementRepository.deleteMeasurementsBySensorStation(sensorStation);
        //detach Plants from sensor station
        plantRepository.detachFromSensorStation(sensorStation);
        //unassign gardeners from sensor station
        sensorStation.getGardener().forEach(x->this.removeGardenerFromSensorStation(sensorStation,x));
        //delete Sensors
        sensorRepository.deleteAllBySensorStationEquals(sensorStation);

        sensorStationRepository.delete(sensorStation);
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(getCurrentUser());
        deleteLog.setSubject("SENSOR STATION DELETION");
        deleteLog.setText("DELETED SENSOR STATION: " + sensorStation.getSensorStationID());
        deleteLog.setType(LogType.SUCCESS);
        logRepository.save(deleteLog);
    }


    public long getSensorStationsAmount() {
        return sensorStationRepository.count();
    }


    public Collection<String> getAllSensorStationsIds() {//this is used to return a list of sensorstations ids to delete from when deleting measurements
        return sensorStationRepository.getAllSensorStationsIds();
    }

    /**
     * Method to get the name of the user currently logged in.
     * This is needed for logging.
     * @return username.
     */
    public String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            return authentication.getName();
        }
        return null;
    }
}
