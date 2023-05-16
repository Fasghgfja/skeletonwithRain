package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the SensorStation model class.
 */
class SensorStationTest {

    /**
     * Testing getters and setters for the SensorStation model.
     */
    @Test
    void testGetterAndSetter(){
        SensorStation sensorStation = new SensorStation();

        String id = "TestSensorStation";
        String location = "Location";
        String alarmSwitch = "Off";
        String description = "Description";
        Plant plant = new Plant();
        AccessPoint accessPoint = new AccessPoint();
        Userx gardener = new Userx();
        gardener.setRoles(Set.of(UserRole.GARDENER));
        Set<Userx> gardeners = new HashSet<>();
        Integer alarmCountThreshold = 12;

        assertNull(sensorStation.getSensorStationName());
        assertNull(sensorStation.getId());
        assertNull(sensorStation.getSensorStationID());
        assertNull(sensorStation.getLocation());
        assertNull(sensorStation.getAlarmSwitch());
        assertNull(sensorStation.getDescription());
        assertNull(sensorStation.getPlant());
        assertNull(sensorStation.getAccessPoint());
        assertEquals(gardeners, sensorStation.getGardener());
        assertNull(sensorStation.getAlarmCountThreshold());

        gardeners.add(gardener);

        sensorStation.setSensorStationID(id);
        sensorStation.setSensorStationName(id);
        sensorStation.setLocation(location);
        sensorStation.setAlarmSwitch(alarmSwitch);
        sensorStation.setDescription(description);
        sensorStation.setPlant(plant);
        sensorStation.setAccessPoint(accessPoint);
        sensorStation.setGardener(gardeners);
        sensorStation.setAlarmCountThreshold(alarmCountThreshold);

        assertEquals(id, sensorStation.getId());
        assertEquals(id, sensorStation.getSensorStationID());
        assertEquals(id, sensorStation.getSensorStationName());
        assertEquals(location, sensorStation.getLocation());
        assertEquals(alarmSwitch, sensorStation.getAlarmSwitch());
        assertEquals(description, sensorStation.getDescription());
        assertEquals(plant, sensorStation.getPlant());
        assertEquals(accessPoint, sensorStation.getAccessPoint());
        assertEquals(gardeners, sensorStation.getGardener());
        assertEquals(alarmCountThreshold, sensorStation.getAlarmCountThreshold());
        assertEquals(id, sensorStation.toString());
    }

    /**
     * Testing compareTo() method of the SensorStation model.
     */
    @Test
    void testCompareTo(){
        SensorStation sensorStation1 = new SensorStation();
        SensorStation sensorStation2 = new SensorStation();

        sensorStation1.setSensorStationName("SensorStation1");
        sensorStation2.setSensorStationName("SensorStation2");

        assertTrue(sensorStation1.compareTo(sensorStation2) < 0);
        assertTrue(sensorStation2.compareTo(sensorStation1) > 0);
    }
}
