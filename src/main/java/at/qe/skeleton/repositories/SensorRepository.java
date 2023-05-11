package at.qe.skeleton.repositories;


import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.api.model.BoarderValueFrame;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;

public interface SensorRepository extends AbstractRepository<Sensor, Long>{

    Sensor findFirstById(Long id);

    List<Sensor> findSensorsBySensorStation(SensorStation sensorStation);

    long count();

    @Query("SELECT COUNT (s) FROM Sensor s WHERE s.sensorStation = :sensorStation")
    Integer countSensors(@Param("sensorStation") SensorStation sensorStation);

    Sensor findFirstBySensorStationAndType( SensorStation sensorStation,String type);

    @Query("select s.id, s.lower_border, s.upper_border, s.sensorStation.sensorStationName from" +
            " Sensor s inner join SensorStation ss on s.sensorStation.sensorStationName = ss.sensorStationName " +
            "where ss.accessPoint.accessPointID = :accessPoint")
    ArrayList<BoarderValueFrame> getBoarderValuesByAccespointID(@Param("accessPoint")Long id);
}
