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
    public SSInterval getFirstByAccessPointId(Long acId){
        return intervalRepository.findFirstByAccessPointId(acId);
    }
    public SSInterval getFirstByAccessPoint(AccessPoint accessPoint){
        return intervalRepository.findFirstByAccessPoint(accessPoint);
    }


    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public SSInterval saveInterval(SSInterval interval) {
        return intervalRepository.save(interval);
    }


    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public void deleteInterval(SSInterval interval) {
        intervalRepository.delete(interval);
    }

    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public void deleteIntervalByAccessPoint(AccessPoint accessPoint) {
        intervalRepository.deleteIntervalByAccessPoint(accessPoint);
    }

    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public void deleteIntervalByAccessPointId(Long acId) {
        intervalRepository.deleteIntervalByAccessPointId(acId);
    }

}
