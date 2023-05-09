package at.qe.skeleton.services;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the measurementService.
 */

@SpringBootTest
@WebAppConfiguration
class MeasurementServiceTest {

    @Autowired
    private MeasurementService measurementService;

    /**
     * Testing the getAllMeasurements() method of the measurementService.
     */
    @DirtiesContext
    @Test
    void testGetAllMeasurements(){
        int initialAmountOfMeasurements = measurementService.getMeasurementsAmount();

        if(initialAmountOfMeasurements > 0){
            assertNotNull(measurementService.getAllMeasurements());
            assertEquals(initialAmountOfMeasurements, measurementService.getAllMeasurements().size());
        }
        else{
            assertThrows(NullPointerException.class, measurementService::getAllMeasurements);
        }
    }

    /**
     * Testing the getMeasurementsAmount() method of the measurementService.
     */
    @DirtiesContext
    @Test
    void testGetMeasurementsAmount(){
        if(measurementService.getAllMeasurements() != null){
            assertEquals(measurementService.getAllMeasurements().size(),measurementService.getMeasurementsAmount());
        }
        else {
            assertEquals(0, measurementService.getMeasurementsAmount());
        }
    }

    /**
     * Testing the getMeasurementStatus() method of the measurementService.
     * Here we test what happens if the value is empty or null.
     */
    @DirtiesContext
    @Test
    void testGetMeasurementStatusForValueNull(){
        String measurementValue1 = null;
        String measurementValue2 = "";
        assertEquals("OK", measurementService.getMeasurementStatusForValue(measurementValue1,null));
        assertEquals("OK", measurementService.getMeasurementStatusForValue(measurementValue2,null));
    }

    /**
     * Testing the checkThreshold() method for the type 'SOIL_MOISTURE' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdSoilMoisture(){
        String type = "SOIL_MOISTURE";
        String measurmentValue1 = "100";
        String measurmentValue2 = "5";
        String measurmentValue3 = "50";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the checkThreshold() method for the type 'HUMIDITY' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdHumidity(){
        String type = "HUMIDITY";
        String measurmentValue1 = "81";
        String measurmentValue2 = "5";
        String measurmentValue3 = "50";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the checkThreshold() method for the type 'AIR_PRESSURE' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdAirPressure(){
        String type = "AIR_PRESSURE";
        String measurmentValue1 = "2.5";
        String measurmentValue2 = "0.2";
        String measurmentValue3 = "1.5";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the checkThreshold() method for the type 'TEMPERATURE' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdTemperatur(){
        String type = "TEMPERATURE";
        String measurmentValue1 = "37";
        String measurmentValue2 = "5";
        String measurmentValue3 = "23";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the checkThreshold() method for the type 'AIR_QUALITY' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdAirQuality(){
        String type = "AIR_QUALITY";
        String measurmentValue1 = "37";
        String measurmentValue2 = "5";
        String measurmentValue3 = "55";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the checkThreshold() method for the type 'LIGHT_INTENSITY' of the measurementService.
     * The first two values exceed the threshold. The third value is within the given threshold.
     */
    @DirtiesContext
    @Test
    void testCheckThresholdLightIntensity(){
        String type = "LIGHT_INTENSITY";
        String measurmentValue1 = "2933";
        String measurmentValue2 = "5";
        String measurmentValue3 = "1200";

        assertEquals(1, measurementService.checkThreshold(measurmentValue1, type));
        assertEquals(1, measurementService.checkThreshold(measurmentValue2, type));
        assertEquals(0, measurementService.checkThreshold(measurmentValue3, type));
    }

    /**
     * Testing the getMeasurementStatus() method of the measurementService.
     * Here we test what happens if the value that is not null.
     */
    @DirtiesContext
    @Test
    void testGetMeasurementStatusForValueNotNull(){
        String measurementValueSoilMoistureOK = "50";
        String measurementValueHumidityOK = "75";
        String measurementValueHumidityNotOK = "90";
        assertEquals("OK", measurementService.getMeasurementStatusForValue(measurementValueSoilMoistureOK,"SOIL_MOISTURE"));
        assertEquals("OK", measurementService.getMeasurementStatusForValue(measurementValueHumidityOK,"HUMIDITY"));
        assertEquals("Wrong", measurementService.getMeasurementStatusForValue(measurementValueHumidityNotOK,"HUMIDITY"));
    }

//TODO: measurementThresholds redo

}
