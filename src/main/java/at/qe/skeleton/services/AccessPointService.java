package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.IPCantReadException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Scope("application")
public class AccessPointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessPointService.class);

    @Autowired
    private AccessPointRepository accessPointRepository;
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
            LOGGER.error("error", e);
        }

        return accessPoint1;
    }

    /**
     * This method is used to creat a config.yaml for a new accessPoint
     * @param accessPoint is an object
     */
    private void createYaml(AccessPoint accessPoint, boolean isNew) throws IOException {
        if(isNew){
            File file = new File("./Hardware_src/Raspberry/config.yaml");
            try (FileWriter writer = new FileWriter(file)) {


                String content = String.format("""
                        accesspoint-params:
                          id: %d
                          validation: False
                        webapp-params:
                          ip: %s:%d
                          pswd: passwd
                          usnm: admin""", accessPoint.getAccessPointID(), getIP(), 8080);//webServerAppCtxt.getWebServer().getPort()
                writer.write(content);
                writer.flush();
            } catch (IOException | IPCantReadException e) {
                LOGGER.error("error", e);
            }
        }
    }

    /**
     * This method is used to get the current ip address
     * @return
     */
    private String getIP() throws IPCantReadException {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {

            String ip = "8.8.8.8";
            datagramSocket.connect(InetAddress.getByName(ip), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            throw new IPCantReadException(e.getMessage());
        }
    }
    /**
     * Deletes an access point and creates a deleted log.
     * @param accessPoint
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        accessPointRepository.delete(accessPoint);
    }

}
