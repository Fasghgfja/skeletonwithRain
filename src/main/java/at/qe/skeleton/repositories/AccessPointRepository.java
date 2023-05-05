package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AccessPointRepository extends AbstractRepository<AccessPoint, Long>{
    AccessPoint findFirstById(Long id);

    //AccessPoint findByLocation(String location);

    //AccessPoint findByValidatedTrue();

    @Query("SELECT a.accessPointID FROM AccessPoint a")
    List<Long> getAllAccessPointsId();
    @Override
    <S extends AccessPoint> S save(S entity);

    long count();
}
