package at.qe.skeleton.services;

import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.ImageRepository;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;


/**
 * Service for accessing and manipulating plant data.
 */
@Service
@Scope("application")
public class PlantService {
    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private SensorStationService sensorStationService;


    /**
     * Method to get the amount of all plants currently stored in the database.
     * @return amount of all plants.
     */

    public Long getPlantsAmount() {
        return plantRepository.count();
    }

    /**
     * The method Returns a collection of all plants.
     */
    @PreAuthorize("permitAll()")
    public Collection<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    /**
     * The method Loads a single plant identified by its plantName.
     *
     * @param plantID the ID of the deck to load
     * @return the plant with the given ID
     */
    @PreAuthorize("permitAll()")
    public Plant loadPlant(Long plantID) {
        return plantRepository.findFirstById(plantID);//TODO:check which one to use
    }

    /**
     * The method Saves a single plant.
     * If the plant is new then creation Date will be saved along , if it is not then
     * the method will change the UpdateDate for this plant.
     *
     * @param plant the plant to save.
     * @return the saved plant.
     */
    @PreAuthorize("permitAll()")
    public Plant savePlant(Plant plant) {
        if (plant.getPlantedDate() == null) {
            plant.setPlantedDate(LocalDate.now());
        }
        return plantRepository.save(plant);
    }

    /**
     * Deletes the plant.
     * @param plant the plant to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePlant(Plant plant) {
        plantRepository.delete(plant);
    }

    /**
     * Deletes a plant that is currently in a sensor station.
     * This first removes the plant from the sensor station and then deletes it.
     * @param plant to be deleted
     */
    @PreAuthorize("permitAll()") // TODO: Not implemented but tested
    public void deletePlantWithStation(Plant plant) {

        SensorStation sensorStation = plant.getSensorStation();
        sensorStation.setPlant(null);
        sensorStationService.saveSensorStation(sensorStation);
        savePlant(plant);
        deletePlant(plant);
    }

    /**
     * This method detaches all images assigned to a plant from the plant and keeps them stored in the database.
     * @param plant The plant that all images should be detached from.
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    public void detachAllImagesFromPlant(Plant plant) {
        imageRepository.setPlantIdToNull(plant);
    }

    /**
     * Method to get all followed plants for a specific user.
     * @param user to get the followed plants for.
     * @return A Collection of all plants followed by the given user.
     */

    public Collection<Plant> getFollowedPlants(Userx user) {
        return plantRepository.findPlantsByFollowers(user);
    }

    //TODO:find plant by gardener

    public Collection<Plant> getOnlyPlantsNotYetFollowed(Userx user) {
        return plantRepository.findPlantsInPlantsCatalogueNotYetFollowed(user.getUsername());
    }

    /**
     * The method gets the plants from their names.
     */
    public Collection<String> getAllPlantsUniqueNames() {
        return plantRepository.findAllPlantsUniqueNames();
    }

    /**
     * Get all the names of the plants that are currently not linked to a sensor station.
     * @return Collection of names.
     */
    public Collection<String> getAllNotUsedPlantsUniqueNames() {
        return plantRepository.findAllNotUsedPlantsUniqueNames();
    }

    /**
     * Method to check if a plant is already follwed by a user.
     * @param currentUser the user currently logged in.
     * @param plant the plant to check if it is already followed.
     * @return boolean that represents wheter the plant is followed or not.
     */

    @PreAuthorize("permitAll()")
    public Boolean isPlantAlreadyFollowed(Userx currentUser, Plant plant) {
        return plantRepository.findPlantsByFollowers(currentUser).contains(plant) ;
    }

    /**
     * Deletes all plants plants that are not linked to a sensor station.
     * It also detaches all the images that are linked to this plant and keeps them in te database.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAllPlantsWithoutSensorStation() {
        HashSet<Plant> toDelete = plantRepository.findAllBySensorStationEquals(null);
        toDelete.forEach(this::detachAllImagesFromPlant);
        plantRepository.deleteAllBySensorStationEquals(null);
    }
}


