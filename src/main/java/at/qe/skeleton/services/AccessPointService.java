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

    /**
     * Method to get all access points currently stored in the database.
     * @return Collection of all access points.
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public Collection<AccessPoint> getAllAccessPoint() {
        return accessPointRepository.findAll();
    }

    /**
     * Method to get a list of all access point id´s currently stored in the database.
     * @return List of all access point id´s.
     */
    public List<Long> getAllAccessPointIds() {
        return accessPointRepository.getAllAccessPointsId();
    }

    /**
     * Get the access point with the given id from the database.
     * @param id of the access point
     * @return Accesspoint with the given id.
     */
    public AccessPoint getFirstById(Long id){
        return accessPointRepository.findFirstById(id);
    }


    /**
     * Method to get the amount of access points currently stored in the database.
     * @return amount of access points.
     */
    public long getAccessPointsAmount(){
        return accessPointRepository.count();
    }

    /**
     * Method to load an access point.
     * @param id of the access point to load.
     * @return Access point with given id.
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')" )
    public AccessPoint loadAccessPoint(Long id) {
        return accessPointRepository.findFirstById(id);
    }

    /**
     * Method to save an access point to the database.
     * @param accessPoint the access point to save.
     * @return the saved access point.
     */
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
