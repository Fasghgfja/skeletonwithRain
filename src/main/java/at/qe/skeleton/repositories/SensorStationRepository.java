package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorStation;


public interface SensorStationRepository extends AbstractRepository<SensorStation, Long>{
    SensorStation findFirstById(Long id);

    SensorStation findByLocation(String location);


}
