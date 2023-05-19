package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Scope("application")
public class AccessPointService {

    @Autowired
    private AccessPointRepository accessPointRepository;
    /*
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;
    */
    /**
     * Method to get all access points currently stored in the database.
     * @return Collection of all access points.
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')")
    public Collection<AccessPoint> getAllAccessPoint() {
        return accessPointRepository.findAll();
    }

    /**
     * Method to get a list of all access point id´s currently stored in the database.
     * @return List of all access point id´s.
     */
    public List<Long> getAllAccessPointIds() {
        return accessPointRepository.getAllAccessPointsId();
    }

    /**
     * Get the access point with the given id from the database.
     * @param id of the access point
     * @return Accesspoint with the given id.
     */
    public AccessPoint getFirstById(Long id){
        return accessPointRepository.findFirstById(id);
    }


    /**
     * Method to get the amount of access points currently stored in the database.
     * @return amount of access points.
     */
    public long getAccessPointsAmount(){
        return accessPointRepository.count();
    }

    /**
     * Method to load an access point.
     * @param id of the access point to load.
     * @return Access point with given id.
     */
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('GARDENER')" )
    public AccessPoint loadAccessPoint(Long id) {
        return accessPointRepository.findFirstById(id);
    }

    /**
     * Method to save an access point to the database.
     * @param accessPoint the access point to save.
     * @return the saved access point.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccessPoint saveAccessPoint(AccessPoint accessPoint) {
        boolean isNew = false;
        if (accessPoint.isNew()) {
            accessPoint.setCreateDate(LocalDate.now());
            isNew = true;
        } else {
            accessPoint.setUpdateDate(LocalDate.now());
        }
        AccessPoint accessPoint1 = accessPointRepository.save(accessPoint);

        try {
            createYaml(accessPoint1, isNew);
        }catch (IOException e){
            e.printStackTrace();
        }

        return accessPoint1;
    }

    /**
     * This method is used to creat a config.yaml for a new accessPoint
     * @param accessPoint
     */
    private void createYaml(AccessPoint accessPoint, boolean isNew) throws IOException {
        if(isNew){
            File file = new File("./Hardware_src/Raspberry/config.yaml");
            FileWriter writer = new FileWriter(file);
            file.setWritable(file.setReadable(file.setExecutable(true)));
            System.out.println();
            String content = String.format("""
                    accesspoint-params:
                      id: %d
                      measurement-intervall: 1
                      webapp-intervall: 1
                      alarmCountThreshold: 1
                      validation: False
                    webapp-params:
                      ip: %s:%d
                      pswd: passwd
                      usnm: admin""", accessPoint.getAccessPointID(), getIP(),8080 );//webServerAppCtxt.getWebServer().getPort()
            writer.write(content);
            writer.flush();
            writer.close();
        }
    }

    /**
     * This method is used to get the current ip address
     * @return
     */
    private String getIP(){
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Deletes an access point and creates a delete log.
     * @param accessPoint
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        accessPointRepository.delete(accessPoint);
    }

}
