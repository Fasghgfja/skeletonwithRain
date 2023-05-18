package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class PlantTest {

    /**
     * Testing the toString method of the plant model.
     */
    @Test
    void testToString(){
        Plant plant = new Plant();
        plant.setPlantName("PlantName");
        assertEquals("PlantName", plant.toString());
        assertNotEquals("NOTPlantName", plant.toString());
    }

    /**
     * Testing the compareTo() method of the plant model.
     */
    @Test
    void testCompareTo(){
        Plant plant1 = new Plant();
        Plant plant2 = new Plant();
        plant1.setPlantID(1L);
        plant2.setPlantID(2L);
        assertTrue(plant1.compareTo(plant2) < 0);
        assertTrue(plant2.compareTo(plant1) > 0);
    }
}
