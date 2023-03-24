package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.*;

/**
 * The class is responsible for handling the file upload functionality in the application.
 * It receives the uploaded file, saves it in the local file system, and also saves it in the database as an {@link Image}.
 * and performs the necessary actions. Upon successful upload, a {@code FacesMessage} is displayed.
 * This class is dependent on the {@link ImageService}, which is injected using Spring's {@code @Autowired} annotation.
 */
@Component
@Scope("session")
public class FileUploadController implements Serializable {

    @Autowired
    private transient ImageService imageService;


    /**
     * Handles the uploaded file by converting it to a byte array, saving it as an {@link Image} object using the
     * {@link ImageService}, and displaying a {@code FacesMessage} upon successful upload.
     * @param event the file upload event triggered by the user */
 public void handleFileUpload(FileUploadEvent event) throws IOException {
            FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            UploadedFile file = event.getFile();

            // Saving in the database
            // Using ByteArrayOutputStream to directly save the file as a byte array in the database
            InputStream in = file.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int read;
            byte[] bytes = new byte[50000000];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            byte[] fileBytes = out.toByteArray();
            out.close();

            // Saving the byte array in the Image object and persisting it using imageService
            Image image = new Image();
            image.setImageByte(fileBytes);
            imageService.saveImage(image);
        }


}


