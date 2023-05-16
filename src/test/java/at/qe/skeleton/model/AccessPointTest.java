package at.qe.skeleton.model;

import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for the AccessPoint model.
 */
@SpringBootTest
@WebAppConfiguration
public class AccessPointTest {

    @Autowired
    AccessPointService accessPointService;

    @Autowired
    UserService userService;

    /**
     * Testing the getId() method of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testGetId(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint = accessPointService.saveAccessPoint(accessPoint);

        assertNotNull(accessPoint.getAccessPointID());
        assertNotNull(accessPoint.getId());
        assertEquals(accessPoint.getAccessPointID(), accessPoint.getId());
    }

    /**
     * Testing the hashCode() method of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testHashCode(){
        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1 = accessPointService.saveAccessPoint(accessPoint1);
        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2 = accessPointService.saveAccessPoint(accessPoint2);

        assertEquals(accessPoint1.hashCode(), accessPoint1.hashCode());
        assertNotEquals(accessPoint1.hashCode(), accessPoint2.hashCode());
    }

    /**
     * Testing the compareTo() method of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testCompareTo(){
        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1 = accessPointService.saveAccessPoint(accessPoint1);
        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2 = accessPointService.saveAccessPoint(accessPoint2);

        assertTrue(accessPoint1.compareTo(accessPoint2) < 0);
        assertTrue(accessPoint2.compareTo(accessPoint1) > 0);
    }

    /**
     * Testing the toString() method of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testToString(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint = accessPointService.saveAccessPoint(accessPoint);

        assertEquals(accessPoint.getId().toString(), accessPoint.toString());
    }

    /**
     * Testing the equals() method of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testEquals(){
        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1 = accessPointService.saveAccessPoint(accessPoint1);
        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2 = accessPointService.saveAccessPoint(accessPoint2);
        Userx user1 = new Userx();
        user1.setUsername("user1");
        user1 = userService.saveUser(user1);

        assertNotEquals(null, accessPoint1);
        assertNotEquals(accessPoint1, accessPoint2);
        assertNotEquals(accessPoint1, user1);
    }

    /**
     * Testing the setter and getter for location of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testGetLocation(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint = accessPointService.saveAccessPoint(accessPoint);

        assertNull(accessPoint.getLocation());
        accessPoint.setLocation("testLocation");
        accessPoint = accessPointService.saveAccessPoint(accessPoint);
        assertEquals("testLocation", accessPoint.getLocation());
    }

    /**
     * Testing the setter and getter for validated of the accessPoint model.
     */
    @DirtiesContext
    @Test
    @WithMockUser(username="admin", authorities = {"ADMIN"})
    void testValidated(){
        AccessPoint accessPoint = new AccessPoint();
        accessPoint = accessPointService.saveAccessPoint(accessPoint);

        assertFalse(accessPoint.isValidated());
        accessPoint.setValidated(true);
        accessPoint = accessPointService.saveAccessPoint(accessPoint);
        assertTrue(accessPoint.isValidated());
    }


}
