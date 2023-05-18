package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.*;
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
     *
     * @return Collection of all sensor stations.
     */
    public Collection<SensorStation> getAllSensorStations() {
        return sensorStationRepository.findAll();
    }

    /**
     * Method to get all sensor stations assigned to the corresponding user with user role "GARDENER".
     *
     * @param user the user with role "GARDENER"
     * @return Colletion of all assigned sensor stations.
     */

    public Collection<SensorStation> getAllAssignedSensorStations(Userx user) {
        return sensorStationRepository.findSensorStationsByGardener(user);
    }

    /**
     * Method to assign a gardener to a sensor station.
     *
     * @param sensorStation the sensor station that the gardener will be assigned to.
     * @param user          the user with role "GARDENER".
     */

    public void addGardenerToSensorStation(SensorStation sensorStation, String user) {
        if (user == null || sensorStation == null) {
            return;
        }
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        Userx userload = userRepository.findFirstByUsername(user);

        userload.getSensorStationsUnderCare().add(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
        userRepository.save(userload);
    }

    /**
     * Method to remove a gardener from an assigned sensor station.
     *
     * @param sensorStation the sensor station that the gardener will be removed from.
     * @param user          the user with role "GARDENER".
     */
    public void removeGardenerFromSensorStation(SensorStation sensorStation, Userx user) {
        if (user == null || sensorStation == null) {
            return;
        }
        Userx userload = userRepository.findFirstByUsername(user.getId());
        sensorStation = sensorStationRepository.findFirstById(sensorStation.getSensorStationName());
        userload.getSensorStationsUnderCare().remove(sensorStation);
        Set<Userx> gardeners = new HashSet<>(userRepository.findUserxBySensorStationsUnderCareIsContaining(sensorStation));
        sensorStation.setGardener(gardeners);
        userRepository.save(userload);
    }

    /**
     * Method to remove an access point from a sensor station.
     * @param accessPoint the access point to be removed.
     */

    public void removeAccessPointFromSensorStations(AccessPoint accessPoint) {
        if (accessPoint == null) {
            return;
        }
        AccessPoint accessLoad = accessPointRepository.findFirstById(accessPoint.getId());
        sensorStationRepository.setAccessPointToNull(accessLoad);
    }

    /**
     * Loads a sensor station from the database.
     * @param id of the sensor station to be loaded.
     * @return the sensor station with the given ID.
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public SensorStation loadSensorStation(String id) {
        return sensorStationRepository.findFirstById(id);
    }

    /**
     * Saves a entitiy of type SensorStation to the database.
     * @param sensorStation the sensor station to be saved.
     * @return the saved sensor station.
     */

    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public SensorStation saveSensorStation(SensorStation sensorStation) {
        if (sensorStation.isNew()) {
            sensorStation.setCreateDate(LocalDate.now());
        } else {
            sensorStation.setUpdateDate(LocalDate.now());
        }
        return sensorStationRepository.save(sensorStation);
    }

    /**
     * Deletes a sensor station. Also deletes all measurements of the sensor station and detaches the plant if the sensor
     * station has a plant in it.
     * Gardeners assigned to the sensor station will also be removed from it.
     * @param sensorStation the sensor station to be deleted.
     */

    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public void deleteSensorStation(SensorStation sensorStation) {

        measurementRepository.deleteMeasurementsBySensorStation(sensorStation);
        plantRepository.detachFromSensorStation(sensorStation);
        sensorStation.getGardener().forEach(x -> this.removeGardenerFromSensorStation(sensorStation, x));
        sensorRepository.deleteAllBySensorStationEquals(sensorStation);

        sensorStationRepository.delete(sensorStation);

        Log deleteLog = new Log();
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(getCurrentUser());
        deleteLog.setSubject("SENSOR STATION DELETION");
        deleteLog.setText("DELETED SENSOR STATION: " + sensorStation.getSensorStationID());
        deleteLog.setType(LogType.SUCCESS);
        logRepository.save(deleteLog);
    }


    /**
     * Gets the amount of sensor stations currently stored in the database.
     * @return long of the amount of sensor stations.
     */
    public long getSensorStationsAmount() {
        return sensorStationRepository.count();
    }

    /**
     * Gets a collection of strings containing all ids of all sensor stations stored in the database.
     * @return Collection of ids as strings.
     */
    public Collection<String> getAllSensorStationsIds() {//this is used to return a list of sensorstations ids to delete from when deleting measurements
        return sensorStationRepository.getAllSensorStationsIds();
    }

    /**
     * Method to get the name of the user currently logged in.
     * This is needed for logging.
     *
     * @return username.
     */
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
