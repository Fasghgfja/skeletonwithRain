package at.qe.skeleton.model;

import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.services.PlantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the Image model.
 */
@SpringBootTest
@WebAppConfiguration
class ImageTest {

    @Autowired
    ImageService imageService;

    @Autowired
    PlantService plantService;

    /**
     * Testing the getter and setter method for field id of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testGetId(){
        Image image1 = new Image();
        image1.setId(200L);
        assertEquals(200, image1.getId());
    }

    /**
     * Testing the getter and setter method for field plant of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testPlantSetterAndGetter(){
        Plant plant = plantService.savePlant(new Plant());

        Image image1 = new Image();
        assertNull(image1.getPlant());
        image1.setPlant(plant);
        image1 = imageService.saveImage(image1);
        assertEquals(plant, image1.getPlant());
    }

    /**
     * Testing the getter and setter method for field imageName of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testImageNameSetterAndGetter(){
        Image image1 = new Image();
        assertNull(image1.getImageName());
        image1.setImageName("image1");
        image1 = imageService.saveImage(image1);
        assertEquals("image1", image1.getImageName());
    }

    /**
     * Testing the getter and setter method for field creationDate of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testCreationDateSetterAndGetter(){
        Image image1 = new Image();
        Image image2 = new Image();
        assertNull(image1.getDate());
        assertNull(image1.getCreationDate());
        image1.setCreationDate(LocalDate.now());
        image2.setDate(LocalDate.now());
        image1 = imageService.saveImage(image1);
        assertEquals(LocalDate.now(), image1.getCreationDate());
        assertEquals(LocalDate.now(), image1.getDate());
        assertEquals(LocalDate.now(), image2.getDate());
    }

    /**
     * Testing the getter and setter method for field imageByte of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testImageByteSetterAndGetter(){
        Image image1 = new Image();
        assertNull(image1.getImageByte());
        byte[] testbytes = new byte[] { 0x12, 0x34, 0x56, 0x78, (byte) 0x9A };
        image1.setImageByte(testbytes);
        image1 = imageService.saveImage(image1);
        assertEquals(testbytes, image1.getImageByte());
    }

    /**
     * Testing the getter and setter method for field approved of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testApprovedSetterAndGetter(){
        Image image1 = new Image();
        assertFalse(image1.isApproved());
        image1.setApproved(true);
        image1 = imageService.saveImage(image1);
        assertTrue(image1.isApproved());
    }

    /**
     * Testing the hashCode() method of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testHashCode(){
        Image image1 = new Image();
        image1 = imageService.saveImage(image1);
        Image image2 = new Image();
        image2 = imageService.saveImage(image2);

        assertEquals(image1.hashCode(), image1.hashCode());
        assertNotEquals(image1.hashCode(), image2.hashCode());
    }

    /**
     * Testing the hashCode() method of the Image model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testEquals(){
        Image image1 = new Image();
        image1 = imageService.saveImage(image1);
        Image image2 = new Image();
        image2 = imageService.saveImage(image2);
        Plant plant = plantService.savePlant(new Plant());

        assertEquals(image1, image1);
        assertNotEquals(image1, image2);
        assertNotEquals(null, image1);
        assertNotEquals(image1, plant);
    }



}
