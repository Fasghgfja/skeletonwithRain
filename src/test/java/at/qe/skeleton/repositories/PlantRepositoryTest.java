package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Plant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
public class PlantRepositoryTest {
    @Autowired
    PlantRepository plantRepository;

    @Autowired
    UserxRepository userxRepository;


    private static Plant plant;

    @BeforeAll
    public static void init() {
        plant = new Plant();
        plant.setPlantID(1L);
        plant.setPlantName("Plant_Test");
    }

    @Test
    @DirtiesContext
    void testDelete() {
        Plant plantToDelete = plantRepository.findFirstByPlantID(2L);
        assertNotNull(plantToDelete);
        plantRepository.delete(plantToDelete);
        assertNull(plantRepository.findFirstByPlantID(2L));
    }

    @Test
    @DirtiesContext
    void testSave() {
        Plant plant4 = new Plant();
        plant4.setPlantName("Sour Tsunami");
        plant4.setCreateDate(LocalDate.now());
        plant4.setPlantID(52L);
        plantRepository.save(plant4);
        assertEquals(plant4,plantRepository.findFirstByPlantName("Sour Tsunami"));
    }

    @Test
    @DisplayName("Testing FindById success.")
    void testFindById() {
        assertTrue(plantRepository.findById(1L).isPresent());
        assertEquals(plantRepository.findById(1L).get(), plant);
    }

    @Test
    @DisplayName("Testing FindById failure.")
    void testFail_FindById() {
        assertTrue(plantRepository.findById(100L).isEmpty());
        assertTrue(plantRepository.findById(200L).isEmpty());
    }

    @Test
    @DisplayName("Testing if FindAll correctly gives back all saved plants.")
    void testFindAll() {
        assertEquals(3,plantRepository.findAll().size());
    }

    @Test
    @DisplayName("Testing FindFirstByPlantName success.")
    void testFindFirstByPlantName() {
        assertEquals("Atomic Northern Lights",plantRepository.findFirstByPlantName("Atomic Northern Lights").getPlantName());
        assertEquals(0,plantRepository.findFirstByPlantName("Atomic Northern Lights").getPlantID());
        assertEquals(LocalDate.of(2022,1,1),plantRepository.findFirstByPlantName("Atomic Northern Lights").getCreateDate());
        assertNull(plantRepository.findFirstByPlantName("Atomic Northern Lights").getUpdateDate());
    }

    @Test
    @DisplayName("Testing FindFirstByPlantName failure.")
    void testFailure_FindFirstByPlantName() {
        assertNull(plantRepository.findFirstByPlantName("Deck4000"));
        assertNull(plantRepository.findFirstByPlantName("ck4"));
        assertNull(plantRepository.findFirstByPlantName("Deck"));
    }

    @Test
    void testFindFirstByPlantID() {
        assertEquals(plantRepository.findFirstByPlantID(1L), plant);
        assertEquals(4L,plantRepository.findFirstByPlantID(4L).getPlantID());
        assertNull(plantRepository.findFirstByPlantID(400L));
    }

    @Test
    void testFail_FindFirstByPlantID() {
        assertEquals(plantRepository.findFirstByPlantID(1L), plant);
    }


    @ParameterizedTest(name = "FindBy PlantName Containing({1}) => found:{0}")
    @CsvSource({"1,Atomic","1,Northern Lights","3,o","1,Alpine","0,Northern Lights1","0,Atomis","0,10Atomic2","0,A3tomic","1,Alpine Rocket"})
    @DisplayName("test success for FindbyPlantName")
    void testFindByPlantNameContaining(int expected,String nameContains) {
        assertEquals(expected,plantRepository.findByPlantNameContaining(nameContains).size());
    }


}

