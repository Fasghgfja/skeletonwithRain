package at.qe.skeleton.repositories;



import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;

import java.util.List;

public interface SensorRepository extends AbstractRepository<Sensor, Long>{

    Sensor findFirstById(Long id);

    List<Sensor> findSensorsBySensorStation(SensorStation sensorStation);


    long count();


}
