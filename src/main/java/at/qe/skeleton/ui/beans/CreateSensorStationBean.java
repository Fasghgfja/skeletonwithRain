package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
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
public class CreateSensorStationBean implements Serializable {

    @Autowired transient SensorStationService sensorStationPointService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;


    private String location;
    private String sensorStationName;
    private String alarmSwitch;
    private String description;



    public void doCreateNewCreateSensorStation(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setLocation(location);
        sensorStation.setSensorStationName(sensorStationName);
        sensorStation.setAlarmSwitch(alarmSwitch);
        sensorStation.setDescription(description);
        sensorStation = sensorStationPointService.saveSensorStation(sensorStation);

        //TODO: log sensor station creation
        //Log createLog = new Log();
        //createLog.setDate(LocalDate.now());
       // createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        //createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
       // createLog.setSubject("AP CREATION");
        //createLog.setText("CREATED AP: " + sensorStation.getId());
        //createLog.setType(LogType.SUCCESS);
        //logRepository.save(createLog);

    }
}
