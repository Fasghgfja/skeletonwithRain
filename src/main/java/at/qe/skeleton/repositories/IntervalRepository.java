package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SSInterval;
import at.qe.skeleton.model.SensorStation;
import jakarta.transaction.Transactional;



public interface IntervalRepository extends AbstractRepository<SSInterval, Long> {


    SSInterval findFirstById(Long id);

    SSInterval findFirstByAccessPoint(AccessPoint accessPoint);

    SSInterval findFirstByAccessPointId (Long accessPointId);


    @Transactional
    void deleteIntervalByAccessPoint(AccessPoint accessPoint);

    @Transactional
    void deleteIntervalByAccessPointId(Long accessPointId);



}


