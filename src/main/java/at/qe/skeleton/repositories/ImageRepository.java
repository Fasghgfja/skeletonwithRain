package at.qe.skeleton.repositories;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for managing image entities {@link Image}.
 * Provides basic methods for loading, saving and removing entities as inherited from the Abstract Repository
 * and adds methods to find cards by CardId  {@link ImageRepository#findFirstById(Long)} and
 * By Deck {@link ImageRepository#findImagesByPlant(Plant)}.
 * The Derived Query are split into two parts separated by keywords:
 * The first one is the introducer(e.g find.., read.., query.., ...)
 * The second one defines the criteria (e.g ...ByName, ...).
 */
public interface ImageRepository extends AbstractRepository<Image, Long> {

    /**
     * Retrieves an image entity by its id.
     * @param imageID must not be {@literal null}.
     * @return The card with the given id.
     * @throws IllegalArgumentException If imageID is {@literal null}.
     */
    Image findFirstById(Long imageID);
    Image findFirstByPlant(Plant plant);


    /**
     * Retrieves all images from a plant.
     * @param plant must not be {@literal null}.
     * @return The images belonging to the selected plant as a List.
     * @throws IllegalArgumentException If plant is {@literal null}.
     */
    List<Image> findImagesByPlant(Plant plant);


    List<Image> findImagesByPlantAndApprovedEquals(Plant plant,boolean approved);

    List<Image> findImagesByApprovedEquals(boolean approved);

    /**
     * Method to get a collection of all images that are assigned to plants.
     */
    @Query("SELECT DISTINCT im FROM Image im WHERE im.plant IS NOT NULL AND im.approved = :value")
    List<Image> findPlantLinkedImagesAndApprovedEquals(@Param("value") boolean value );









    /**
     * Retrieves all images from a plant created before or equal to the given date
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If plant is {@literal null}.
     */
    List<Image> findImagesByPlantAndCreationDateLessThanEqual(Plant plant, LocalDate date);

    /**
     * Retrieves all images from a plant created after the given date
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If deck is {@literal null}.
     */
    List<Image> findImagesByPlantAndCreationDateGreaterThan(Plant plant, LocalDate date);

    /**
     * Retrieves all images from a plant created at a given date
     * @param plant must not be {@literal null}.
     * @param date the date after which it filters, must not be {@literal null}
     * @return IllegalArgumentException If deck is {@literal null}.
     */
    List<Image> findImagesByPlantAndCreationDateEquals(Plant plant, LocalDate date);

}

