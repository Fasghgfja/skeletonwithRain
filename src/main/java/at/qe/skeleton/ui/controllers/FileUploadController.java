package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * The class is responsible for handling the file upload functionality in the application.
 * It receives the uploaded file, saves it in the local file system, and also saves it in the database as an {@link Image}.
 * and performs the necessary actions. Upon successful upload, a {@code FacesMessage} is displayed.
 * This class is dependent on the {@link ImageService}, which is injected using Spring's {@code @Autowired} annotation.
 */
@Component
@Named
@Scope("application")
public class FileUploadController implements Serializable {

    @Autowired
    private transient ImageService imageService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Handles the uploaded file by converting it to a byte array, saving it as an {@link at.qe.skeleton.model.Image} object using the
     * {@link ImageService}, and displaying a {@code FacesMessage} upon successful upload.
     * @param event the file upload event triggered by the user */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String id = externalContext.getRequestParameterMap().get("id");


        System.out.println("Page id: " + id);

        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        UploadedFile file = event.getFile();

        System.out.println(file.getFileName());

        // Read the input image into a BufferedImage
        InputStream in = file.getInputStream();
        BufferedImage inputImage = ImageIO.read(in);
        in.close();

        // Resize the input image to a fixed size
        int targetWidth = 800;
        int targetHeight = 600;
        java.awt.Image scaledImage = inputImage.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH);

        // Create a new BufferedImage and draw the scaled image on it
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.createGraphics().drawImage(scaledImage, 0, 0, null);

        // Write the output image to a byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", out);
        byte[] fileBytes = out.toByteArray();
        out.close();

        // Save the byte array in the Image object and persist it using imageService
        Image image = new at.qe.skeleton.model.Image();
        image.setImageByte(fileBytes);

        // Save the picture to the corresponding plant
        if(!id.equals("")){
            imageService.addPictureToPlantPictures(image,id);
            System.out.println("picture saved" + id);
            return ;
        }
        System.out.println("picture saved" + id);
        imageService.saveImage(image);

    }



}


