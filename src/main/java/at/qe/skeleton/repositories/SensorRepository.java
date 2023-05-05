package at.qe.skeleton.repositories;



import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorRepository extends AbstractRepository<Sensor, Long>{

    Sensor findFirstById(Long id);

    List<Sensor> findSensorsBySensorStation(SensorStation sensorStation);

    long count();

    @Query("SELECT COUNT (s) FROM Sensor s WHERE s.sensorStation = :sensorStation")
    Integer countSensors(@Param("sensorStation") SensorStation sensorStation);



}
