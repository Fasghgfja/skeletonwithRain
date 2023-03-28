package at.qe.skeleton.services;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Scope("application")
public class AccessPointService {

    @Autowired
    private AccessPointRepository accessPointRepository;
    @Autowired
    private LogRepository logRepository;
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<AccessPoint> getAllAccessPoint() {
        return accessPointRepository.findAll();
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        Log deleteLog = new Log();

        deleteLog.setDate(LocalDate.now());
        deleteLog.setSubject("ACCESS POINT DELETION");
        deleteLog.setText("DELETED ACCESS POINT: " + accessPoint.getId());

        logRepository.save(deleteLog);
        accessPointRepository.delete(accessPoint);
    }
}
