package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import at.qe.skeleton.services.SensorStationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Getter
@Setter
@Component
@Scope("view")
public class SSIntervalController implements Serializable {

    private static final String DEFAULT = "Default";
    @Autowired
    private transient IntervalService intervalService;
    @Autowired
    private transient SensorStationService sensorStationService;


    private Integer measurementInterval;
    private Integer webAppInterval;

    private SSInterval sSinterval;
    private String ssId;


    public void doSaveInterval() {
        sSinterval = new SSInterval();
        SensorStation sensorStation = sensorStationService.loadSensorStation(ssId);
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(ssId);

        if (oldInterval == null) {
            sSinterval.setSensorStation(sensorStation);
            if (measurementInterval != null) {
                sSinterval.setMeasurementInterval(measurementInterval.toString());
            }
            if (webAppInterval != null) {
                sSinterval.setWebAppInterval(webAppInterval.toString());
            }
            sSinterval = this.intervalService.saveInterval(sSinterval);
        } else {
            if (measurementInterval != null) {
                oldInterval.setMeasurementInterval(measurementInterval.toString());
            }
            if (webAppInterval != null) {
                oldInterval.setWebAppInterval(webAppInterval.toString());
            }
            this.intervalService.saveInterval(oldInterval);
        }
    }

    public String getActualMeasurementIntervalForSensorStation(String sensorStationId) {
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(sensorStationId);
        if (oldInterval != null) {
            if (oldInterval.getMeasurementInterval() == null) {
                return DEFAULT;
            }
            return oldInterval.getMeasurementInterval();
        } else return DEFAULT;
    }

    public String getActualWebAppIntervalForSensorStation(String sensorStationId) {
        SSInterval oldInterval = intervalService.getFirstBySensorStationId(sensorStationId);
        if (oldInterval != null) {
            if (oldInterval.getWebAppInterval() == null) {
                return DEFAULT;
            }
            return oldInterval.getWebAppInterval();
        } else return DEFAULT;
    }


}



