package at.qe.skeleton.services;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.ImageRepository;
import at.qe.skeleton.repositories.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for accessing and managing image data.
 *  The class is annotated with @Scope("application") to ensure
 *  that a single instance of the UserService is created for the entire application.
 */
@Service
@Scope("application")
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlantRepository plantRepository;

    //TODO: Autowire plant repository to search by plant id and so


    /**
     * Returns a collection of all images.
     */
    @PreAuthorize("permitAll()")
    public List<Image> getAllImages() {
        return imageRepository.findAll();
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
     * @param image   the image to save
     * @return the saved image
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }


    public void addPictureToPlantPictures(Image image, String plantid) {
        System.out.println("im image service here plant id " + plantid);
        Plant plant = plantRepository.findFirstByPlantID(Long.parseLong(plantid));
        System.out.println("plant is " + plant);
        if(image == null || plant == null ) {
            System.out.println("addPictureToPlantError null");
            return;}
        image.setPlant(plant);
        imageRepository.save(image);

    }


    /**
     * Deletes the image.
     * @param image the image to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }



}
