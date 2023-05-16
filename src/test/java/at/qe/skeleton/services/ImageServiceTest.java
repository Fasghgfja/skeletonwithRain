package at.qe.skeleton.services;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.repositories.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


/**
 * This is a test class for the ImageService class.
 */
@SpringBootTest
@WebAppConfiguration
class ImageServiceTest {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PlantService plantService;

    /**
     * Testing the saveImage method of the ImageService.
     */
    @DirtiesContext
    @Test
    void testSaveImageWithoutPermission(){
        Image image1 = new Image();
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> imageService.saveImage(image1));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveImageWithPermission(){
        int initialAmount = imageRepository.count();
        Image image1 = new Image();
        imageService.saveImage(image1);
        assertEquals(initialAmount + 1, imageRepository.count());
        Image image2 = new Image();
        imageService.saveImage(image2);
        assertEquals(initialAmount + 2, imageRepository.count());

    }

    /**
     * Method to test getAllImages method of the ImageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllImages(){
        List<Image> initialListOfImages = imageService.getAllImages();

        initialListOfImages.add(imageService.saveImage(new Image()));
        initialListOfImages.add(imageService.saveImage(new Image()));
        initialListOfImages.add(imageService.saveImage(new Image()));

        assertEquals(initialListOfImages, imageService.getAllImages());
    }

    /**
     * Testing the getApprovedImages() method of the ImageService.
     * We create 3 images of which 2 will be set to approved.
     * Then we check if only these 2 get returned.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetApprovedImages(){
        List<Image> initialListOfImages = imageService.getApprovedImages();

        Image image1 = new Image();
        image1.setApproved(true);
        imageService.saveImage(image1);
        Image image2 = new Image();
        image2.setApproved(true);
        imageService.saveImage(image2);
        Image image3 = new Image();
        image3.setApproved(false);
        imageService.saveImage(image3);

        initialListOfImages.add(image1);
        initialListOfImages.add(image2);

        assertEquals(initialListOfImages, imageService.getApprovedImages());
    }

    /**
     * Testing the getNotApprovedImages() method of the ImageService.
     * We create 3 images of which 2 will be set to approved.
     * Then we check if only one not approved gets returned.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetNotApprovedImages(){
        List<Image> initialListOfImages = imageService.getApprovedImages();

        Image image1 = new Image();
        image1.setApproved(true);
        imageService.saveImage(image1);
        Image image2 = new Image();
        image2.setApproved(true);
        imageService.saveImage(image2);
        Image image3 = new Image();
        image3.setApproved(false);
        imageService.saveImage(image3);

        initialListOfImages.add(image3);

        assertEquals(initialListOfImages, imageService.getNotApprovedImages());
    }

    /**
     * Testing the getAllPlantImages() method of the ImageService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPlantImages(){

        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setPlant(plant1);
        Image image2 = new Image();
        image2.setPlant(plant1);
        imageService.saveImage(image2);
        Image image3 = new Image();
        image3.setPlant(plant2);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertTrue(imageService.getAllPlantImages("1").contains(image1));
        assertTrue(imageService.getAllPlantImages("1").contains(image2));
        assertTrue(imageService.getAllPlantImages("2").contains(image3));
        assertFalse(imageService.getAllPlantImages("1").contains(image3));
        assertFalse(imageService.getAllPlantImages("2").contains(image1));
        assertFalse(imageService.getAllPlantImages("2").contains(image2));
    }

    /**
     * Testing the doGetAllApprovedPlantImages() method of the ImageService.
     * Image1 is approved and linked to a plant, therefore should be contained in the list returned
     * by the doGetAllApprovedImages() method.
     * Image2 is not approved and image3 is approved but not linked to a plant.
     * Therefore, image2 and image3 should not be returned by doGetAllApprovedPlantImages().
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDoGetAllApprovedPlantImages(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setPlant(plant1);
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertTrue(imageService.doGetAllApprovedPlantImages().contains(image1));
        assertFalse(imageService.doGetAllApprovedPlantImages().contains(image2));
        assertFalse(imageService.doGetAllApprovedPlantImages().contains(image3));
    }

    /**
     * Testing the getAllPlantImages(String plantId) method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPlantImagesWithPlantId() {
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setPlant(plant1);
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertTrue(imageService.getAllPlantImages("1").contains(image1));
        assertTrue(imageService.getAllPlantImages("1").contains(image2));
        assertFalse(imageService.getAllPlantImages("2").contains(image3));
        assertFalse(imageService.getAllPlantImages("1").contains(image3));
    }

    /**
     * Testing the getAllPlantImagesNotYetApproved(String plantId) method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPlantImagesNotYetApproved() {
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setPlant(plant1);
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertTrue(imageService.getAllPlantImagesNotYetApproved("1").contains(image2));
        assertFalse(imageService.getAllPlantImagesNotYetApproved("1").contains(image1));
        assertFalse(imageService.getAllPlantImagesNotYetApproved("1").contains(image3));
        assertFalse(imageService.getAllPlantImagesNotYetApproved("2").contains(image3));
    }

    /**
     * Testing the getAllApprovedPlantImages(String plantId) method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllApprovedPlantImages() {
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setPlant(plant1);
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertTrue(imageService.getAllApprovedPlantImages("1").contains(image1));
        assertFalse(imageService.getAllApprovedPlantImages("1").contains(image2));
        assertFalse(imageService.getAllApprovedPlantImages("1").contains(image3));
        assertFalse(imageService.getAllApprovedPlantImages("2").contains(image3));
    }

    /**
     * Testing the loadImage(Long imageId) method of the imageService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadImage(){
        Image image1 = new Image();
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertEquals(image1, imageService.loadImage(image1.getId()));
        assertEquals(image2, imageService.loadImage(image2.getId()));
        assertEquals(image3, imageService.loadImage(image3.getId()));
        assertNull(imageService.loadImage(0L));
    }

    /**
     * Testing the deleteImage(Image image) method of the imageService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteImage() {
        Image image1 = new Image();
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setApproved(false);
        Image image3 = new Image();
        image3.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        int amountBeforeDeleting = imageService.getTotalImagesAmount();
        imageService.deleteImage(image1);
        assertEquals(amountBeforeDeleting - 1, imageService.getTotalImagesAmount());
        imageService.deleteImage(image2);
        assertEquals(amountBeforeDeleting - 2, imageService.getTotalImagesAmount());
        assertTrue(imageService.getTotalImagesAmount() > 0);
    }

    /**
     * Testing the deleteAllImagesByPlant(Plant plant) method of the imageService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCountAllImagesByPlant() {
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        Plant plant2 = new Plant();
        plant2.setPlantID(2L);
        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        Image image1 = new Image();
        image1.setApproved(true);
        image1.setPlant(plant1);
        Image image2 = new Image();
        image2.setApproved(false);
        image2.setPlant(plant1);
        Image image3 = new Image();
        image3.setApproved(true);
        image3.setPlant(plant2);
        int amountBeforeSavingPlant1 = imageService.getAllPlantImages("1").size();
        int amountBeforeSavingPlant2 = imageService.getAllPlantImages("2").size();

        imageService.saveImage(image1);
        imageService.saveImage(image2);
        imageService.saveImage(image3);

        assertEquals(amountBeforeSavingPlant1 + 2, imageService.countAllImagesByPlant(plant1));
        assertEquals(amountBeforeSavingPlant2 + 1, imageService.countAllImagesByPlant(plant2));

        imageService.deleteImage(image1);
        assertEquals(amountBeforeSavingPlant1 + 1, imageService.countAllImagesByPlant(plant1));
    }

    /**
     * Testing the addPicturesToPlantPictures(Image image, String plantID) method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAddPicturesToPlantPictures(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        plantService.savePlant(plant1);

        Image image1 = new Image();
        Image image2 = new Image();

        imageService.saveImage(image1);
        imageService.saveImage(image2);

        int amountBeforeAdding = imageService.countAllImagesByPlant(plant1);
        imageService.addPictureToPlantPictures(image1, "1");
        assertEquals(amountBeforeAdding + 1, imageService.countAllImagesByPlant(plant1));
        imageService.addPictureToPlantPictures(image2, "1");
        assertEquals(amountBeforeAdding + 2, imageService.countAllImagesByPlant(plant1));
    }

    /**
     * Testing the getTotalImagesAmount() method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetTotalImagesAmount(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        plantService.savePlant(plant1);

        int initialAmountOfImages = imageRepository.count();

        Image image1 = new Image();
        image1.setPlant(plant1);
        Image image2 = new Image();

        imageService.saveImage(image1);
        imageService.saveImage(image2);

        assertEquals(initialAmountOfImages + 2, imageService.getTotalImagesAmount());
    }

    /**
     * Testing the getApprovedImagesAmount() method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetApprovedImagesAmount(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        plantService.savePlant(plant1);

        int initialAmountOfImages = imageRepository.count();

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();

        imageService.saveImage(image1);
        imageService.saveImage(image2);

        assertEquals(initialAmountOfImages + 1, imageService.getApprovedImagesAmount());
        assertNotEquals(initialAmountOfImages, imageService.getApprovedImagesAmount());
        assertNotEquals(initialAmountOfImages, imageService.getApprovedImagesAmount() + 2);
    }

    /**
     * Testing the getNotApprovedImagesAmount() method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetNotApprovedImagesAmount(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        plantService.savePlant(plant1);

        int initialAmountOfImages = imageRepository.count();

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();

        imageService.saveImage(image1);
        imageService.saveImage(image2);

        assertEquals(initialAmountOfImages + 1, imageService.getNotApprovedImagesAmount());
        assertNotEquals(initialAmountOfImages, imageService.getNotApprovedImagesAmount());
        assertNotEquals(initialAmountOfImages, imageService.getNotApprovedImagesAmount() + 2);
    }

    /**
     * Testing the getApprovedImagesNoPlantAmount() method of the imageService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetApprovedImagesNoPlantAmount(){
        Plant plant1 = new Plant();
        plant1.setPlantID(1L);
        plantService.savePlant(plant1);

        int initialAmountOfImages = imageRepository.count();

        Image image1 = new Image();
        image1.setPlant(plant1);
        image1.setApproved(true);
        Image image2 = new Image();
        image2.setApproved(true);

        imageService.saveImage(image1);
        imageService.saveImage(image2);

        assertEquals(initialAmountOfImages + 1, imageService.getApprovedImagesNoPlantAmount());
        assertNotEquals(initialAmountOfImages, imageService.getApprovedImagesNoPlantAmount());
        assertNotEquals(initialAmountOfImages, imageService.getApprovedImagesNoPlantAmount() + 2);
    }






}
