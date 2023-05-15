package at.qe.skeleton.repositories;

import at.qe.skeleton.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface SensorStationRepository extends AbstractRepository<SensorStation, String>{
    SensorStation findFirstById(String sensorStationName);


    long count();

    SensorStation findByLocation(String location);

    /**
     * Method to get a collection of all sensor stations assigned to a given gardener.
     * @param username the gardener for which to retrieve the sensor stations.
     */
    List<SensorStation> findSensorStationsByGardener(@Param("gardener") Userx username );

    List<SensorStation> findAllByAccessPoint_AccessPointID(Long id);

    @Query("SELECT s.sensorStationName FROM SensorStation s")
    Collection<String> getAllSensorStationsIds();

    @Transactional
    @Modifying
    @Query("UPDATE SensorStation ss SET ss.accessPoint = null WHERE ss.accessPoint = :accessPoint")
    void setAccessPointToNull(@Param("accessPoint") AccessPoint accessPoint);



    /**
     * Method to get a collection of all sensor stations assigned to a given gardener.
     * @param username the gardener for which to retrieve the sensor stations.

    @Query("SELECT DISTINCT ss FROM SensorStation ss WHERE ss.plant IN ( SELECT p FROM Plant p JOIN p.gardeners gp WHERE gp.username = :gardener )")
    List<SensorStation> findSensorStationsByGardener(@Param("gardener") String username );
     */

}
