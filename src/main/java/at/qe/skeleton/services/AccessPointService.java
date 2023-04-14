package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.sql.RowSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
@Scope("application")
public class AccessPointService {

    @Autowired
    private AccessPointRepository accessPointRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserxRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<AccessPoint> getAllAccessPoint() {
        return accessPointRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        Log deleteLog = new Log();
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(getAuthenticatedUser().getUsername());
        deleteLog.setSubject("AP DELETION");
        deleteLog.setText("DELETED AP: " + accessPoint.getId());
        deleteLog.setType(LogType.SUCCESS);
        logRepository.save(deleteLog);
        accessPointRepository.delete(accessPoint);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessPoint createAccessPoint(AccessPoint accessPoint, String location){

        accessPoint.setLocation(location);
        accessPoint.setValidated(false);
        AccessPoint accessPointToBeCreated = saveAccessPoint(accessPoint);


        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(getAuthenticatedUser().getUsername());
        createLog.setSubject("AP CREATION");
        createLog.setText("CREATED AP ID: " + accessPointToBeCreated.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
        return accessPointToBeCreated;
    }

    /**
     * This Method returns the actual instance of the user.
     * @return the authenticated user.
     */
    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }
}
