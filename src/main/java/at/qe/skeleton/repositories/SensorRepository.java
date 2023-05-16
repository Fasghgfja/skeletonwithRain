package at.qe.skeleton.repositories;


import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface SensorRepository extends AbstractRepository<Sensor, Long>{

    Sensor findFirstById(Long id);

    List<Sensor> findSensorsBySensorStation(SensorStation sensorStation);

    long count();

    @Query("SELECT COUNT (s) FROM Sensor s WHERE s.sensorStation = :sensorStation")
    Integer countSensors(@Param("sensorStation") SensorStation sensorStation);

    Sensor findFirstBySensorStationAndType( SensorStation sensorStation,String type);

    @Transactional
    void deleteAllBySensorStationEquals(SensorStation sensorStation);


}
