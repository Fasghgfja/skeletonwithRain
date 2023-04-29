package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.LogService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Getter
@Setter
@Component
@Scope("view")
public class AccessPointController implements Serializable {

    @Autowired
    private transient AccessPointService accessPointService;

    @Autowired
    private transient LogService logService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    private AccessPoint accessPoint;
    private String location;

    /**
     * Returns a list of all access points.
     * @return
     */
    public Collection<AccessPoint> getAccessPoints(){
        return accessPointService.getAllAccessPoint();
    }

    /**
     * Returns the amount of all exisiting access points.
     */
    public long getAccessPointsAmount(){
        return accessPointService.getAccessPointsAmount();
    }

    public void doSaveAccessPoint(){
            accessPoint = this.accessPointService.saveAccessPoint(accessPoint);
    }

    /**
     * Deletes an access point.
     */

    public void doDeleteAccessPoint(){
        Log deleteLog = new Log();
        deleteLog.setDate(LocalDate.now());
        deleteLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        deleteLog.setAuthor(sessionInfoBean.getCurrentUserName());
        deleteLog.setSubject("AP DELETION");
        deleteLog.setText("DELETED AP: " + accessPoint.getId());
        deleteLog.setType(LogType.SUCCESS);
        logService.saveLog(deleteLog);
        accessPointService.deleteAccessPoint(accessPoint);
        accessPoint = null;
    }
}

