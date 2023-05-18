package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.SensorStation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The class is responsible for handling sensor stations lists.
 */
@Getter
@Setter
@Component
@Scope("view")
public class SensorStationListController implements Serializable {

    private List<SensorStation> sensorStationList;
    private List<SensorStation> assignedSensorStationList;

    @Autowired
    private transient SensorStationService sensorService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private Collection<SensorStation> filteredSensorStations;

    /**
     * Caching the lists of sensor stations and assigned sensor stations in order to filter and sort them.
     */
    @PostConstruct
    public void init() {
        sensorStationList = (ArrayList<SensorStation>) sensorService.getAllSensorStations();
        assignedSensorStationList = (ArrayList<SensorStation>) sensorService.getAllAssignedSensorStations(getSessionInfoBean().getCurrentUser());
    }

    /**
     * Returns a list of all sensor stations.
     */
    public Collection<SensorStation> getSensorStations() {
        return sensorService.getAllSensorStations();
    }

    /**
     * Returns a collection of all sensor station ids as strings.
     * @return Collection of string containing all ids.
     */

    public Collection<String> getSensorStationsIds() {
        return sensorService.getAllSensorStationsIds();
    }

    /**
     * Returns a list of all sensor stations assigned to a gardener.
     */
    public Collection<SensorStation> getAssignedSensorStations() {
        Userx user = sessionInfoBean.getCurrentUser();
        return sensorService.getAllAssignedSensorStations(user);
    }

    /**
     * Returns how many sensor stations are registered in the system.
     */
    public long getSensorStationsAmount() {
        return sensorService.getSensorStationsAmount();
    }

}
