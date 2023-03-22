package at.qe.skeleton.services;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.repositories.ImageRepository;
import at.qe.skeleton.repositories.PlantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service for accessing and manipulating image data.
 */
@Service
@Scope("application")
public class ImageService {

    //TODO:Only for testing , replace with ImageRepository access
    List<Image> pictures;


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlantRepository plantRepository;

    //TODO:implement backend with database , method only made to test frontend
    /**
     * Returns a collection of all images.
     */
    @PostConstruct
    public void init() {
        pictures = new ArrayList<>();

        pictures.add(new Image("../resources/images/logoTop.svg"));
        pictures.add(new Image("../resources/images/logoTop.svg"));
    }

    @PreAuthorize("permitAll()")
    public List<Image> getAllImages() {
        return pictures;
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
