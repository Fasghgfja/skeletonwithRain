package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.SSInterval;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.IntervalService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Component
@Scope("view")
public class CreateAccessPointBean implements Serializable {

    @Autowired transient AccessPointService accessPointService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private IntervalService intervalService;


    @Autowired
    private transient LogRepository logRepository;

    private String location;

    public void doCreateNewAccessPoint(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setValidated(false);
        accessPoint.setLocation(location);
        SSInterval interval = new SSInterval();
        interval.setWebAppInterval("1");
        interval.setMeasurementInterval("1");

        accessPoint = accessPointService.saveAccessPoint(accessPoint);
        interval.setAccessPoint(accessPoint);
        intervalService.saveInterval(interval);

        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
        createLog.setSubject("AP CREATION");
        createLog.setText("CREATED AP: " + accessPoint.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);

    }
}
