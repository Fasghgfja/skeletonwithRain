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
 * Service for accessing and manipulating image data.
 */
@Service
@Scope("application")
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlantRepository plantRepository;


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
        return imageRepository.findById(1L).orElse(null);
    }

    /**
     * Saves a single image.
     * If the image is new then creation date will be saved along, if it is not then
     * the method will change the update date for this image.
     *
     * @param image   the image to save
     * @param plantId the ID of the plant associated with this image
     * @return the saved image
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Image saveImage(Image image) {
      //  Plant plant = plantRepository.findById(plantId).orElse(null);
      //  if (plant == null) {
         //   return null;
       //}
      //  image.setPlant(plant);
        return imageRepository.save(image);
    }


    /**
     * Deletes the image.
     *
     * @param image the image to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteImage(Image image) {
        Plant plant = image.getPlant();
        plantRepository.save(plant);
        imageRepository.delete(image);
    }
}
