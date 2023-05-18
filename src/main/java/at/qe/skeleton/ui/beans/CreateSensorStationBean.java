package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.SensorStationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class is used to create a new sensor station from the sensor station management page.
 * By default the alarm switch of the sensor station is set to "off".
 */
@Getter
@Setter
@Component
@Scope("view")
public class CreateSensorStationBean implements Serializable {

    @Autowired
    transient SensorStationService sensorStationService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;

    private String location;
    private String sensorStationName;
    private String alarmSwitch;
    private String description;
    private Integer alarmCountThreshold;

    public void doCreateNewCreateSensorStation() {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setLocation(location);
        sensorStation.setSensorStationName(sensorStationName);
        sensorStation.setAlarmSwitch("off");
        sensorStation.setDescription(description);
        sensorStation.setAlarmCountThreshold(Objects.requireNonNullElse(alarmCountThreshold, 5));

        sensorStation = sensorStationService.saveSensorStation(sensorStation);

        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("CREATED SENSOR STATION: " + sensorStation.getSensorStationID());
            successFileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUser().getUsername());
        createLog.setSubject("SENSOR STATION CREATION");
        createLog.setText("CREATED SENSOR STATION: " + sensorStation.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }
}
