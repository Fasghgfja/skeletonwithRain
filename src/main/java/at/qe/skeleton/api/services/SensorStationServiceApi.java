package at.qe.skeleton.api.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorApi;
import at.qe.skeleton.api.model.SensorStationApi;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.MeasurementRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.services.SensorService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for managing measurements entities {@link Measurement}.
 * ALL METHODS THAT RETURN MEASUREMENTS WILL DO SO ORDERED BY DATE ; MOST RECENT FIRST
 * Provides methods for loading, saving and removing Measurements
 * and adds methods to find measurements by measurementId  {@link MeasurementRepository#findFirstById(Long)} and
 * By Plant {@link MeasurementRepository#findMeasurementsByPlantOrderByTimestampDesc(Plant)} .
 * among with a additional wide selection of methods to find measurements using different parameters:
 * The Derived Query are split into parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
@Service
public class SensorStationServiceApi {

    @Autowired
    SensorStationRepository sensorStationRepository;
    @Autowired
    SensorService sensorService;

    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, SensorStation> sensorStations = new ConcurrentHashMap<>();
    private static final int NOITEMFOUND = 0;
    private static final int INITVALUE = 0;
    // TODO call sensorStations via senseorStationService, not via sensorStationRepositrory

    /**
     * This method is called to update senorstations
     * @param sensorStation
     * @throws SensorStationNotFoundException
     */
    public void updateSensorStation(SensorStationApi sensorStation) throws SensorStationNotFoundException{
        /*
        SensorStationApi newSensorStation = new SensorStationApi();
        newSensorStation.setName(sensorStation.getName());
        newSensorStation.setService_description(sensorStation.getService_description());
        newSensorStation.setAlarm_switch(sensorStation.getAlarm_switch());

         */
        // read sensorstation
        SensorStation sensorStation1 = sensorStationRepository.findFirstById(sensorStation.getName());
        if (sensorStation1 != null) {
            sensorStation1.setDescription(sensorStation.getService_description());
            sensorStation1.setAlarmSwitch(sensorStation.getAlarm_switch());
            sensorStationRepository.save(sensorStation1);
        }else throw new SensorStationNotFoundException();
        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);
        /*
        System.out.println(newSensorStation);
        SensorStation newSensorStation2 = convertMeasurement(newSensorStation);
        sensorStationRepository.save(newSensorStation2);
        newSensorStation2 = sensorStationRepository.findFirstById(newSensorStation.getName());
        */
    }

    public SensorStation convertMeasurement(SensorStationApi sensorStationApi) {
        SensorStation newSensorStation = new SensorStation();
        newSensorStation.setSensorStationID(sensorStationApi.getName());
        newSensorStation.setLocation("No Location");
        //set alarm swith and implement it in java side
        sensorStationRepository.save(newSensorStation);
        //measurements.put(Long.valueOf(newMeasurement.getSensorStationName()), newMeasurement);
        System.out.println(newSensorStation);
        newSensorStation = sensorStationRepository.findFirstById(newSensorStation.getSensorStationName());
        System.out.println(newSensorStation);
        return newSensorStation;
    }

    /**
     * this method is called to find a sensorStation by a given sensorStation name
     * @param id
     * @return
     * @throws SensorStationNotFoundException
     */
    public String findOneSensorStation(String id) throws SensorStationNotFoundException {
        System.out.println(id);
        SensorStation sensorStation = sensorStationRepository.findFirstById(id);
        if (sensorStation != null)
            return sensorStation.getAlarmSwitch();
        else
            throw new SensorStationNotFoundException();
    }

    /**
     * This method is called to find all sensorStationsby AccessPoint
     * @return
     * @throws SensorStationNotFoundException
     */
    public List<String> findAllSensorStation(Long id) throws SensorStationNotFoundException {
        System.out.println(id);
        List<SensorStation> sensorStations1 = sensorStationRepository.findAllByAccessPoint_AccessPointID(id);

        ArrayList<String> stationNames = new ArrayList<>();
        if( sensorStations1.size() != NOITEMFOUND){
            for (SensorStation s:
                 sensorStations1) {
                System.out.println(s.getSensorStationName());
                stationNames.add(s.getSensorStationName());
            }
            return stationNames;
        }
        else throw new SensorStationNotFoundException();
    }

    /**
     * This method is called to save new Sensors
     * @param sensorApi
     * @return
     * @throws SensorStationNotFoundException
     */
    public int addSensor(SensorApi sensorApi) throws SensorStationNotFoundException{
        Sensor sensor = new Sensor();
        sensor.setId(sensorApi.getSensor_id());
        sensor.setSensorStation(sensorStationRepository.findFirstById(sensorApi.getStation_name()));
        sensor.setUuid(sensorApi.getUuid());
        sensor.setType(sensorApi.getType());
        sensor.setAlarm_count(sensorApi.getAlarm_count());
        sensor.setLower_border(sensorApi.getLowerBoarder());
        sensor.setUpper_border(sensorApi.getUpperBoarder());
        sensorService.saveSensor(sensor);
        // to check if it has been saved successfully
        if(sensorService.loadSensor(sensorApi.getSensor_id()) != null){
            return Response.SC_OK;
        }else throw new SensorStationNotFoundException();
    }

    /**
     * This method is called to update sensor attribute alarm_count
     * @param sensorApi
     * @throws SensorStationNotFoundException
     */
    public void updateSensor(SensorApi sensorApi) throws SensorStationNotFoundException{
        Sensor sensor = sensorService.loadSensor(sensorApi.getSensor_id());
        if(sensor != null){
            sensor.setAlarm_count(sensorApi.getAlarm_count());
            sensorService.saveSensor(sensor);
        }else throw new SensorStationNotFoundException();

    }

    public SensorApi findOneSensor(Long id) throws SensorStationNotFoundException{
        Sensor sensor = sensorService.loadSensor(id);
        System.out.println(sensor.toString());
        SensorApi sensorApi = new SensorApi();
        sensorApi.setSensor_id(sensor.getId());
        sensorApi.setAlarm_count(sensor.getAlarm_count());
        sensorApi.setUpperBoarder(sensor.getUpper_border());
        sensorApi.setLowerBoarder(sensor.getLower_border());
        return sensorApi;
    }
}
