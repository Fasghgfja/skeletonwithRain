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
 *
 * and performs the necessary actions. Upon successful upload, a {@code FacesMessage} is displayed.
 * This class is dependent on the {@link ImageService}, which is injected using Spring's {@code @Autowired} annotation.
 */
@Component
@Scope("session")
public class FileUploadController implements Serializable {

    @Autowired
    private ImageService imageService;


    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        UploadedFile file = event.getFile();

        // Saving in the database
        // The code doesn't save the file in the local file system.
        // Instead, it saves the file as a byte array in the database through the imageService.saveImage() method.
        // The FileOutputStream is used to write the bytes into a file named "file" (which doesn't really exist) .....solve it more elegantly
        // possible solution : ByteArrayOutputStream instead of a FileOutputStream
        //Obtaining bytes
        InputStream in = file.getInputStream();
        OutputStream out = new FileOutputStream(new File("file"));
                // write the inputStream to a FileOutputStream and database
                int read = 0;
                byte[] bytes = new byte[50000000];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                in.close();
                out.flush();
                out.close();
                 //saving
                Image image = new Image();
                image.setImageByte(bytes);
                imageService.saveImage(image);

        }


}


