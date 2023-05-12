package at.qe.skeleton.services;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Plant;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.PlantRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.h2.table.Plan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the PlantService.
 */
@SpringBootTest
@WebAppConfiguration
class PlantServiceTest {

    @Autowired
    PlantService plantService;

    @Autowired
    SensorStationService sensorStationService;
    @Autowired
    ImageService imageService;
    @Autowired
    UserService userService;

    @Autowired
    PlantRepository plantRepository;

    /**
     * Testing the savePlant() and getPlantAmount() methods of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetPlantsAmount(){
        long initialAmount = plantService.getPlantsAmount();

        Plant plant1 = new Plant();
        plant1.setPlantName("testPlant1");
        Plant plant2 = new Plant();

        plantService.savePlant(plant1);
        plantService.savePlant(plant2);

        assertEquals(initialAmount + 2, plantService.getPlantsAmount());
    }

    /**
     * Testing the getAllPlants() methods of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPlants(){
        List<Plant> initialPlantList = (ArrayList<Plant>) plantService.getAllPlants();

        Plant plant1 = new Plant();
        plant1.setPlantName("testPlant1");
        Plant plant2 = new Plant();

        initialPlantList.add(plantService.savePlant(plant1));
        initialPlantList.add(plantService.savePlant(plant2));

        assertEquals(initialPlantList, plantService.getAllPlants());
    }

    /**
     * Testing the loadPlant(Long plantId) methods of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testLoadPlant(){
        Plant plant1 = new Plant();
        plant1.setPlantName("testPlant1");
        Plant plant2 = new Plant();

        Plant loadedPlant1 = plantService.savePlant(plant1);
        Plant loadedPlant2 = plantService.savePlant(plant2);

        assertEquals(loadedPlant1, plantService.loadPlant(loadedPlant1.getPlantID()));
        assertEquals(loadedPlant2, plantService.loadPlant(loadedPlant2.getPlantID()));
    }

    /**
     * Testing the deletePlant(Plant plant) methods of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeletePlant(){
        Plant plant1 = new Plant();
        plant1.setPlantName("testPlant1");
        Plant plant2 = new Plant();

        Plant loadedPlant1 = plantService.savePlant(plant1);
        Plant loadedPlant2 = plantService.savePlant(plant2);

        assertTrue(plantService.getAllPlants().contains(loadedPlant1));
        assertTrue(plantService.getAllPlants().contains(loadedPlant2));

        plantService.deletePlant(loadedPlant1);
        assertFalse(plantService.getAllPlants().contains(loadedPlant1));
    }

    /**
     * Testing the detachAllImagesFromPlant(Plant plant) of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void detachAllImagesFromPlant(){
        Plant plant1 = plantService.savePlant(new Plant());
        Image image1 = new Image();
        image1.setPlant(plant1);
        imageService.saveImage(image1);

        plantService.detachAllImagesFromPlant(plant1);
        assertNull(imageService.loadImage(image1.getId()).getPlant());
    }

    /**
     * Testing the getFollowedPlants(Userx user) method of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetFollowedPlants(){
        Userx user1 = new Userx();
        user1.setUsername("testUser1");
        Userx user2 = new Userx();
        user2.setUsername("testUser2");
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);

        Plant plant1 = plantService.savePlant(new Plant());
        Plant plant2 = plantService.savePlant(new Plant());
        Plant plant3 = plantService.savePlant(new Plant());

        userService.addPlantToFollowedPlants(user1, plant1);
        userService.addPlantToFollowedPlants(user1, plant2);
        userService.addPlantToFollowedPlants(user2, plant3);
        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);
        plant3 = plantService.savePlant(plant3);
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);

        assertTrue(plantService.getFollowedPlants(user1).contains(plant1));
        assertTrue(plantService.getFollowedPlants(user1).contains(plant2));
        assertFalse(plantService.getFollowedPlants(user1).contains(plant3));
        assertFalse(plantService.getFollowedPlants(user2).contains(plant1));
        assertFalse(plantService.getFollowedPlants(user2).contains(plant2));
        assertTrue(plantService.getFollowedPlants(user2).contains(plant3));
    }

    /**
     * Testing the getOnlyPlantsNotYetFollowed(Userx user) method of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetOnlyPlantsNotYetFollowed(){
        Userx user1 = new Userx();
        user1.setUsername("testUser1");
        Userx user2 = new Userx();
        user2.setUsername("testUser2");
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);

        Plant plant1 = plantService.savePlant(new Plant());
        Plant plant2 = plantService.savePlant(new Plant());
        Plant plant3 = plantService.savePlant(new Plant());

        userService.addPlantToFollowedPlants(user1, plant1);
        userService.addPlantToFollowedPlants(user1, plant2);
        userService.addPlantToFollowedPlants(user2, plant3);
        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);
        plant3 = plantService.savePlant(plant3);
        user1 = userService.saveUser(user1);
        user2 = userService.saveUser(user2);

        assertFalse(plantService.getOnlyPlantsNotYetFollowed(user1).contains(plant1));
        assertFalse(plantService.getOnlyPlantsNotYetFollowed(user1).contains(plant2));
        assertTrue(plantService.getOnlyPlantsNotYetFollowed(user1).contains(plant3));
        assertTrue(plantService.getOnlyPlantsNotYetFollowed(user2).contains(plant1));
        assertTrue(plantService.getOnlyPlantsNotYetFollowed(user2).contains(plant2));
        assertFalse(plantService.getOnlyPlantsNotYetFollowed(user2).contains(plant3));
    }

    /**
     * Testing the getAllPlantsUniqueNames() method of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllPlantsUniqueNames(){
        Plant plant1 = new Plant();
        plant1.setPlantName("plant1");
        Plant plant2 = new Plant();
        plant2.setPlantName("plant2");
        Plant plant3 = new Plant();
        plant3.setPlantName("plant3");

        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);
        plant3 = plantService.savePlant(plant3);

        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant1.getPlantName()));
        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant2.getPlantName()));
        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant3.getPlantName()));
    }

    /**
     * Testing the getAllNotUserPlantsUniqueNames() method of the plantService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllNotUsedPlantsUniqueNames(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TestSS");
        sensorStationService.saveSensorStation(sensorStation);
        Plant plant1 = new Plant();
        plant1.setPlantName("plant1");
        Plant plant2 = new Plant();
        plant2.setPlantName("plant2");
        Plant plant3 = new Plant();
        plant3.setPlantName("plant3");
        sensorStation.setPlant(plant1);

        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);
        plant3 = plantService.savePlant(plant3);

        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant1.getPlantName()));
        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant2.getPlantName()));
        assertTrue(plantService.getAllPlantsUniqueNames().contains(plant3.getPlantName()));
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeletePlantWithSensorStation(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TESTSS");
        sensorStation = sensorStationService.saveSensorStation(sensorStation);
        Plant plant1 = new Plant();
        plant1.setPlantName("plant1");
        plant1.setSensorStation(sensorStation);

        plant1 = plantService.savePlant(plant1);
        sensorStation = sensorStationService.saveSensorStation(sensorStation);

        plantService.deletePlantWithStation(plant1);

        assertNull(plantService.loadPlant(plant1.getPlantID()));
        assertNull(sensorStationService.loadSensorStation("TESTSS").getPlant());
    }

    /**
     * Testing the isPlantAlreadyFollowed(Userx currentUser, Plant plant) method of the plantService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testIsPlantAlreadyFollowed() {
        Userx user1 = new Userx();
        user1.setUsername("User1");
        user1 = userService.saveUser(user1);

        Plant plant1 = new Plant();
        plant1.setPlantName("plant1");
        Plant plant2 = new Plant();
        plant2.setPlantName("plant2");
        Plant plant3 = new Plant();
        plant3.setPlantName("plant3");

        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);
        plant3 = plantService.savePlant(plant3);

        userService.addPlantToFollowedPlants(user1, plant1);
        userService.addPlantToFollowedPlants(user1, plant2);

        assertTrue(plantService.isPlantAlreadyFollowed(userService.loadUser("User1"), plant1));
        assertTrue(plantService.isPlantAlreadyFollowed(userService.loadUser("User1"), plant2));
        assertFalse(plantService.isPlantAlreadyFollowed(userService.loadUser("User1"), plant3));
    }

    /**
     * Testing the deleteAllPlantsWithoutSensorStation() method of the plantService.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteAllPlantsWithoutSensorStation(){
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TESTSS");
        sensorStation = sensorStationService.saveSensorStation(sensorStation);

        for (Plant p : plantRepository.findAllBySensorStationEquals(null)){
            p.setSensorStation(sensorStation);
            plantService.savePlant(p);
        }

        Plant plant1 = new Plant();
        plant1.setPlantName("plant1");
        plant1.setSensorStation(sensorStation);
        Plant plant2 = new Plant();
        plant2.setPlantName("plant2");

        plant1 = plantService.savePlant(plant1);
        plant2 = plantService.savePlant(plant2);

        plantService.deleteAllPlantsWithoutSensorStation();

        assertEquals(plant1, plantService.loadPlant(plant1.getPlantID()));
        assertNull(plantService.loadPlant(plant2.getPlantID()));
    }

    /**
     * Method to get all plant names of plants that are currently not in a sensor station.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void getAllNotUsedPlantsUniqueNames(){
        int initialAmount = plantService.getAllNotUsedPlantsUniqueNames().size();
        Plant plant1 = plantService.savePlant(new Plant());
        Plant plant2 = plantService.savePlant(new Plant());
        SensorStation sensorStation = new SensorStation();
        sensorStation.setSensorStationID("TESTSS");
        sensorStation = sensorStationService.saveSensorStation(sensorStation);
        plant2.setSensorStation(sensorStation);
        plantService.savePlant(plant2);

        assertEquals(initialAmount + 1, plantService.getAllNotUsedPlantsUniqueNames().size());

    }
}
