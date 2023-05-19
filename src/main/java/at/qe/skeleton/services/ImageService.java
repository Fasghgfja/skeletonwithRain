package at.qe.skeleton.services;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.repositories.ImageRepository;
import at.qe.skeleton.repositories.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for accessing and managing image data.
 * The class is annotated with @Scope("application") to ensure
 * that a single instance of the ImageService is created for the entire application.
 */
@Service
@Scope("application")
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlantRepository plantRepository;


    /**
     * The Method Returns a collection of all images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    /**
     * The Method Returns a collection of all approved images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getApprovedImages() {
        return imageRepository.findImagesByApprovedEquals(true);
    }

    /**
     * The Method Returns a collection of all not approved images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getNotApprovedImages() {
        return imageRepository.findImagesByApprovedEquals(false);
    }


    /**
     * Method to get a collection of all approved images of all plants.
     * differently as the method following it this takes no parameters.
     */
    @PreAuthorize("permitAll()")
    public List<Image> doGetAllApprovedPlantImages() {
        return imageRepository.findPlantLinkedImagesAndApprovedEquals(true);
    }

    /**
     * Method to get a collection of all images for a given plant.*
     *
     * @param plantId the plant id for which to retrieve the images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getAllPlantImages(String plantId) {
        Plant plant = plantRepository.findFirstByPlantID(Long.parseLong(plantId));
        return imageRepository.findImagesByPlant(plant);
    }

    /**
     * Method to get a collection of all not approverd images for a given plant.*
     *
     * @param plantId the plant id for which to retrieve the images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getAllPlantImagesNotYetApproved(String plantId) {
        Plant plant = plantRepository.findFirstByPlantID(Long.parseLong(plantId));
        return imageRepository.findImagesByPlantAndApprovedEquals(plant, false);
    }

    /**
     * Method to get a collection of all approved images for a given plant.*
     *
     * @param plantId the plant id for which to retrieve the images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getAllApprovedPlantImages(String plantId) {
        Plant plant = plantRepository.findFirstByPlantID(Long.parseLong(plantId));
        return imageRepository.findImagesByPlantAndApprovedEquals(plant, true);
    }


    /**
     * Loads a single image identified by its ID.
     *
     * @param imageId the ID of the image to load
     * @return the image with the given ID
     */
    @PreAuthorize("permitAll()")
    public Image loadImage(Long imageId) {
        return imageRepository.findById(imageId).orElse(null);
    }

    /**
     * Saves a single image.
     * If the image is new then creation date will be saved along, if it is not then
     * the method will change the update date for this image.
     *
     * @param image the image to save
     * @return the saved image
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    /**
     * Deletes the image.
     *
     * @param image the image to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }

    public Integer countAllImagesByPlant(Plant plant) {
        return imageRepository.countImagesByPlant(plant);
    }


    public void addPictureToPlantPictures(Image image, String plantid) {

        Plant plant = plantRepository.findFirstByPlantID(Long.parseLong(plantid));
        if (image == null || plant == null) {
            return;
        }
        image.setPlant(plant);
        imageRepository.save(image);
    }


    public Integer getTotalImagesAmount() {
        return imageRepository.count();
    }

    public Integer getApprovedImagesAmount() {
        return imageRepository.countImagesByApprovedEquals(true);
    }

    public Integer getNotApprovedImagesAmount() {
        return imageRepository.countImagesByApprovedEquals(false);
    }

    public Integer getApprovedImagesNoPlantAmount() {
        return imageRepository.countImagesByApprovedEqualsAndPlantEquals(true, null);
    }


    public long getNotApprovedImagesAmountForPlant(Plant plant) {
        return imageRepository.countImagesByPlantAndApprovedEquals(plant, false);
    }
}
