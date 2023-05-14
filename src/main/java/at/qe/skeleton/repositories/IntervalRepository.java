package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SSInterval;
import at.qe.skeleton.model.SensorStation;
import jakarta.transaction.Transactional;



public interface IntervalRepository extends AbstractRepository<SSInterval, Long> {


    SSInterval findFirstById(Long id);

    SSInterval findFirstBySensorStation(SensorStation sensorStation);

    SSInterval findFirstBySensorStationId (String sensorStationId);


    @Transactional
    void deleteIntervalBySensorStation(SensorStation sensorStation);

    @Transactional
    void deleteIntervalBySensorStationId(String sensorStationId);



}


