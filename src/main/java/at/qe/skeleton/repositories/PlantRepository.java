package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import org.springframework.data.repository.query.Param;
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

    /**
     * The method Retrieves a Plant entity by part of its name.
     *
     * @param plantName must not be {@literal null}.
     * @return The plant containing the given string in its name.
     * @throws IllegalArgumentException If deckName is {@literal null}.
     */
    List<Plant> findByPlantNameContaining(String plantName);

    List<Plant> findPlantsByFollowers(Userx user);


}


