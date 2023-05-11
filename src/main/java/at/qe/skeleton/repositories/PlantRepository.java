package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 * Repository for managing plant entities {@link Plant}.
 * Provides basic methods for loading, saving and removing entities as inherited from the Abstract Repository
 * and adds methods to find plantss using PlantId {@link PlantRepository#findFirstByPlantID(Long)} ,
 * Deck name {@link PlantRepository#findFirstByPlantName(String)} {@link PlantRepository#findByPlantNameContaining(String)}.
 * The Derived Query are split into two parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
public interface PlantRepository extends AbstractRepository<Plant, Long> {

    /**
     * The method Retrieves a Plant entity by its Name.
     *
     * @param plantName must not be {@literal null}.
     * @return The plant with the given name.
     * @throws IllegalArgumentException If PlantName is {@literal null}.
     */
    Plant findFirstByPlantName(String plantName);

    /**
     * The method Retrieves a Plant entity by its ID.
     *
     * @param plantId must not be {@literal null}.
     * @return The plant with the given id.
     * @throws IllegalArgumentException If plantId is {@literal null}.
     */
    Plant findFirstByPlantID(@Param("plantId") Long plantId);

    Plant findFirstById(Long plantId);

    /**
     * The method Retrieves a Plant entity by part of its name.
     *
     * @param plantName must not be {@literal null}.
     * @return The plant containing the given string in its name.
     * @throws IllegalArgumentException If deckName is {@literal null}.
     */
    List<Plant> findByPlantNameContaining(String plantName);

    @Query("SELECT p FROM Plant p LEFT OUTER JOIN p.followers f ON f.username = :username WHERE f.username IS NULL")
    List<Plant> findPlantsInPlantsCatalogueNotYetFollowed(@Param("username") String username );
    List<Plant> findPlantsByFollowers(Userx user);



    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    List<Userx> findByRole(@Param("role") UserRole role);


    long count();


    /**
     * The method is Only used in the scrolldown menu for plant selection.
     */
    @Query("SELECT DISTINCT p.plantName FROM Plant p")
    Collection<String> findAllPlantsUniqueNames();

    @Query("SELECT DISTINCT p.plantName FROM Plant p WHERE p.sensorStation IS NULL")
    Collection<String> findAllNotUsedPlantsUniqueNames();

    HashSet<Plant> findAllBySensorStationEquals(SensorStation sensorStation);

    @Transactional
    void deleteAllBySensorStationEquals(SensorStation sensorStation);
}


