package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Scope("application")
public class AccessPointService {

    @Autowired
    private AccessPointRepository accessPointRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<AccessPoint> getAllAccessPoint() {
        return accessPointRepository.findAll();
    }
    public List<Long> getAllAccessPointIds() {
        return accessPointRepository.getAllAccessPointsId();
    }

    public AccessPoint getFirstById(Long id){
        return accessPointRepository.findFirstById(id);
    }


    public long getAccessPointsAmount(){
        return accessPointRepository.count();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessPoint loadAccessPoint(Long id) {
        return accessPointRepository.findFirstById(id);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessPoint saveAccessPoint(AccessPoint accessPoint) {
        if (accessPoint.isNew()) {
            accessPoint.setCreateDate(LocalDate.now());
        } else {
            accessPoint.setUpdateDate(LocalDate.now());
        }
        return accessPointRepository.save(accessPoint);
    }

    /**
     * Deletes an access point and creates a delete log.
     * @param accessPoint
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        accessPointRepository.delete(accessPoint);
    }

}
