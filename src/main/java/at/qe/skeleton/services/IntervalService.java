package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.IntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;




@Service
@Scope("application")
public class IntervalService {

    @Autowired
    private IntervalRepository intervalRepository;

    public SSInterval getFirstById(Long id){
        return intervalRepository.findFirstById(id);
    }
    public SSInterval getFirstBySensorStationId(String ssId){
        return intervalRepository.findFirstBySensorStationId(ssId);
    }
    public SSInterval getFirstBySensorStation(SensorStation sensorStation){
        return intervalRepository.findFirstBySensorStation(sensorStation);
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    public SSInterval saveInterval(SSInterval interval) {
        return intervalRepository.save(interval);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteInterval(SSInterval interval) {
        intervalRepository.delete(interval);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteIntervalBySensorStation(SensorStation sensorStation) {
        intervalRepository.deleteIntervalBySensorStation(sensorStation);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteIntervalBySensorStationId(String ssid) {
        intervalRepository.deleteIntervalBySensorStationId(ssid);
    }

}
