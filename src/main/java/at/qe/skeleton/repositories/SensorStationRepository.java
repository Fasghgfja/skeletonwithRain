package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SensorStationRepository extends AbstractRepository<SensorStation, String>{
    SensorStation findFirstById(String sensorStationName);


    long count();

    SensorStation findByLocation(String location);

    @Query("SELECT DISTINCT ss FROM SensorStation ss WHERE ss.plant IN ( SELECT p FROM Plant p JOIN p.gardeners gp WHERE gp.username = :gardener )")
    List<SensorStation> findSensorStationsByGardener(@Param("gardener") String username );



}
