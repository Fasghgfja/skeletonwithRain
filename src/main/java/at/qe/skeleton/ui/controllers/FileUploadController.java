package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.annotation.ManagedBean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;



@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {

    @Autowired
    private ImageService imageService;


    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        UploadedFile file = event.getFile();
        // Do what you want with the file
        InputStream in = file.getInputStream();
        OutputStream out = new FileOutputStream(new File("file"));
                // write the inputStream to a FileOutputStream
                int read = 0;
                byte[] bytes = new byte[50000000];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                in.close();
                out.flush();
                out.close();
                Image image = new Image();
                image.setImageByte(bytes);
                imageService.saveImage(image);

        }


}


