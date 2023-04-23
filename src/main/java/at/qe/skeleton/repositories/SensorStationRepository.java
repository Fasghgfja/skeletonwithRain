package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorStation;


public interface SensorStationRepository extends AbstractRepository<SensorStation, String>{
    SensorStation findFirstById(String sensorStationName);


    long count();

    SensorStation findByLocation(String location);


}
