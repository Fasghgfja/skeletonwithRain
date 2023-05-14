package at.qe.skeleton.services;

import at.qe.skeleton.model.AccessPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This is a testing class for the AccessPointService.
 */

@SpringBootTest
@WebAppConfiguration
class AccessPointServiceTest {

    @Autowired
    AccessPointService accessPointService;

    /**
     * Testing saveAccessPoint which just calls the accesspointRepository method save.
     * Then check if the created accesspoint is stored in the database.
     * User has to be logged in and an admin to do this.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testSaveAccessPoint(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setAccessPointID(1L);
        accessPoint.setLocation("Room1");
        accessPoint.setValidated(false);
        accessPointService.saveAccessPoint(accessPoint);

        assertEquals(accessPoint, accessPointService.getFirstById(1L));
    }


    /**
     * Testing the getAccessPointsAmount method.
     * This initializes the starting amount of accesspoints stored in the database.
     * Then a new accesspoint is created and checked if the value increased by 1.
     */

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAccessPointsAmount(){
        long startValue = accessPointService.getAccessPointsAmount();
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setAccessPointID(1L);
        accessPoint.setLocation("Room1");
        accessPoint.setValidated(false);
        accessPointService.saveAccessPoint(accessPoint);

        assertEquals(startValue + 1, accessPointService.getAccessPointsAmount());
    }

    /**
     * Testing the getAllAccessPoints method.
     * The amount of accesspoints is the expected outcome. This method was already tested above.
     * Then we call getAllAccessPoints and check if the size is the same as the amount returned from getAccessPointsAmount.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllAccessPoints(){
        assertEquals(accessPointService.getAccessPointsAmount(), accessPointService.getAllAccessPoint().size());
    }


    /**
     * Testing the delete method.
     * First the amount of current accesspoints in the repository is stored in the variable startValue.
     * Then a new accesspoint is created and checked if this accesspoint now exists and the amount of accesspoints in the database has increased by 1.
     * Then the accesspoint is deleted.
     * Afterwards the getFirstById call should return null and also the amount of accesspoints should be back to the start value.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteAccessPoint(){
        long startValue = accessPointService.getAccessPointsAmount();
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setAccessPointID(1L);
        accessPoint.setLocation("Room1");
        accessPoint.setValidated(false);
        accessPointService.saveAccessPoint(accessPoint);

        assertEquals(accessPoint, accessPointService.getFirstById(1L));
        assertEquals(startValue + 1, accessPointService.getAccessPointsAmount());

        accessPointService.deleteAccessPoint(accessPoint);

        assertNull(accessPointService.getFirstById(1L));
        assertEquals(startValue, accessPointService.getAccessPointsAmount());
    }

    /**
     * Testing getAllAccessPointIds() method of the accessPointService.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllAccessPointIds(){
        int initialAccessPointIds = accessPointService.getAllAccessPointIds().size();

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setAccessPointID(1L);
        accessPointService.saveAccessPoint(accessPoint);

        assertEquals(initialAccessPointIds + 1, accessPointService.getAllAccessPointIds().size());

    }
}
