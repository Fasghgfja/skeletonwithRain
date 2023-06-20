package at.qe.skeleton.api.services;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.*;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.*;
import at.qe.skeleton.services.*;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for managing measurements entities {@link Measurement}.
 * ALL METHODS THAT RETURN MEASUREMENTS WILL DO SO ORDERED BY DATE ; MOST RECENT FIRST
 * Provides methods for loading, saving and removing Measurements
 * and adds methods to find measurements by measurementId  {@link MeasurementRepository#findFirstById(Long)} and
 * among with an additional wide selection of methods to find measurements using different parameters:
 * The Derived Query are split into parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
@Service
public class SensorStationServiceApi {

    @Autowired
    SensorStationRepository sensorStationRepository;
    @Autowired
    SensorStationService sensorStationService;
    @Autowired
    SensorService sensorService;
    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    AccessPointService accessPointService;
    @Autowired
    IntervalService intervalService;
    @Autowired
    LogService logService;
    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, SensorStation> sensorStations = new ConcurrentHashMap<>();
    private static final int NOITEMFOUND = 0;

    /**
     * This method is called to update senorstations
     * @param sensorStation
     * @throws SensorStationNotFoundException
     */
    public void updateSensorStation(SensorStationApi sensorStation) throws SensorStationNotFoundException{

        // read sensorstation
        SensorStation sensorStation1 = sensorStationRepository.findFirstById(sensorStation.getName());
        if (sensorStation1 != null) {
            sensorStation1.setDescription(sensorStation.getService_description());
            sensorStation1.setAlarmSwitch(sensorStation.getAlarm_switch());
            sensorStationRepository.save(sensorStation1);
        }else throw new SensorStationNotFoundException();
    }


    /**
     * this method is called to find a sensorStation by a given sensorStation name
     * @param id
     * @return
     * @throws SensorStationNotFoundException
     */
    public String findOneSensorStation(String id) throws SensorStationNotFoundException {
        SensorStation sensorStation = sensorStationRepository.findFirstById(id);
        if (sensorStation != null)
            return sensorStation.getAlarmSwitch();
        else
            throw new SensorStationNotFoundException();
    }

    /**
     * This method is used to check if a accesspoint is validated
     * @param id
     * @return
     * @throws SensorStationNotFoundException
     */
    public boolean isValidated(Long id) throws AccessPointNotFoundException {
        try {
            AccessPoint toValidateaccessPoint = accessPointService.loadAccessPoint(id);
            return toValidateaccessPoint.isValidated();
        }catch (Exception e){
            throw new AccessPointNotFoundException();
        }

    }
    /**
     * This method is called to find all sensorStationsby AccessPoint
     * @return
     * @throws SensorStationNotFoundException
     */
    public List<String> findAllSensorStation(Long id) throws SensorStationNotFoundException {
        List<SensorStation> sensorStations1 = sensorStationRepository.findAllByAccessPoint_AccessPointID(id);
        ArrayList<String> stationNames = new ArrayList<>();
        if( sensorStations1.size() != NOITEMFOUND){
            for (SensorStation s:
                 sensorStations1) {
                stationNames.add(s.getSensorStationName());
            }
            return stationNames;
        }
        else throw new SensorStationNotFoundException();
    }

    /**
     * This method is called to save new Sensors
     * @param sensorApi list
     * @return
     * @throws SensorNotFoundException
     */
    public void addSensor(List<SensorApi> sensorApi) throws SensorNotFoundException {
        try {
            if (sensorApi.stream().findAny().isPresent() && !sensorService.areSensorsPresent(
                    sensorStationRepository.findFirstById(
                            sensorApi.stream().findFirst().get().getStation_name()))) {
                
                for (SensorApi s :
                        sensorApi) {
                    Sensor sensor = new Sensor();
                    sensor.setSensorStation(sensorStationRepository.findFirstById(s.getStation_name()));
                    sensor.setUuid(s.getUuid());
                    sensor.setType(s.getType());
                    sensor.setAlarm_count(s.getAlarm_count());
                    sensor.setLower_border(s.getLowerBoarder());
                    sensor.setUpper_border(s.getUpperBoarder());
                    sensorService.saveSensor(sensor);
                }
            }
        }catch (Exception e){
            throw new SensorNotFoundException();
        }
    }

    /**
     * This method is called to update sensor attribute alarm_count
     * @param sensorApi list
     * @throws SensorStationNotFoundException
     */
    public void updateSensor(List<SensorApi> sensorApi) throws SensorStationNotFoundException{
        for (SensorApi s:
             sensorApi) {
            Sensor sensor = sensorService.getSensorForSensorStation(
                    sensorStationRepository.findFirstById(s.getStation_name()),s.getType());
            if(sensor != null){
                sensor.setAlarm_count(s.getAlarm_count());
                sensorService.saveSensor(sensor);
            }else throw new SensorStationNotFoundException();
        }
    }

    /**
     * This method is used to call the boarder values for all Sensors they are mapped with an accesspoint via Sensorstation
     * @param id
     * @return
     * @throws SensorStationNotFoundException
     */
    public ArrayList<SensorStationDataFrame> findSensorsByAccesspointID(Long id) throws SensorStationNotFoundException{
        List<SensorStation> sensorStationList = sensorStationRepository.findAllByAccessPoint_AccessPointID(id);
        ArrayList<Sensor> sensorList = new ArrayList<>();
        ArrayList<SensorStationDataFrame> sensorStationDataFrameArrayList = new ArrayList<>();
        for (SensorStation ss:
             sensorStationList) {
            sensorList.addAll(sensorService.getAllSensorsBySensorStation(ss).stream().toList());
        }

        for (Sensor s:
             sensorList) {
            SensorStationDataFrame sensorStationDataFrame = new SensorStationDataFrame();
            sensorStationDataFrame.setSensor_id(s.getId());
            sensorStationDataFrame.setSensorType(s.getType());
            sensorStationDataFrame.setMeasurementInterval(Integer.parseInt(
                    intervalService.getFirstBySensorStation(s.getSensorStation()).getMeasurementInterval()
            ));
            sensorStationDataFrame.setWebappSendInterval(Integer.parseInt(
                    intervalService.getFirstBySensorStation(s.getSensorStation()).getWebAppInterval()
            ));
            sensorStationDataFrame.setAlarmCountThreshold(s.getSensorStation().getAlarmCountThreshold());
            sensorStationDataFrame.setLowerBoarder(s.getLower_border());
            sensorStationDataFrame.setUpperBoarder(s.getUpper_border());
            sensorStationDataFrame.setStation_name(s.getSensorStation().getSensorStationName());
            sensorStationDataFrameArrayList.add(sensorStationDataFrame);
        }
        return sensorStationDataFrameArrayList;
    }

    public void saveNearbyDevices(Long id, List<SensorDevice> devices) throws AccessPointNotFoundException{

        for (SensorDevice sd:
             devices) {
            SensorStation sensorStation = sensorStationService.loadSensorStation(sd.getMac());
            if(sensorStation == null){
                sensorStation = new SensorStation();
                sensorStation.setSensorStationID(sd.getMac());
                sensorStation.setAccessPoint(
                        accessPointService.loadAccessPoint(id)
                );
                sensorStation.setAlarmSwitch("off");
                sensorStation.setAlarmCountThreshold(2);
                SSInterval interval = new SSInterval();
                interval.setSensorStation(sensorStationService.saveSensorStation(sensorStation));
                interval.setWebAppInterval("10");
                interval.setMeasurementInterval("5");
                intervalService.saveInterval(interval);
                System.out.println(sd);
                System.out.println(sensorStation.getId());
            }
            else if(sensorStation.getAccessPoint() == null){
                sensorStation.setAccessPoint(
                        accessPointService.loadAccessPoint(id)
                );
                sensorStationService.saveSensorStation(sensorStation);
            }

        }
    }
    /**
     * This method is used to store the logs of accesspoints
     * @param logFrame
     * @throws SensorStationNotFoundException
     */
    public void saveLog(List<LogFrame> logFrame) throws SensorStationNotFoundException{
        for (LogFrame l:
             logFrame) {
            Log log = new Log();
            log.setAuthor(l.getAuthor());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(l.getTime_stamp(), formatter);
            log.setTime(dateTime);
            log.setText(l.getText());
            log.setSubject(l.getSubject());
            if(l.getType().equals("ERROR"))
                log.setType(LogType.ERROR);
            else if(l.getType().equals("SUCCESS"))
                log.setType(LogType.SUCCESS);
            else
                log.setType(LogType.WARNING);
            logService.saveLog(log);
        }
    }
}
