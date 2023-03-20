package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;


@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {


    @Autowired
    private ImageService imageService;


    public void upload(FileUploadEvent event) throws IOException {
        throw new RuntimeException("Method Reached ! success");
        /**Image image = new Image();
        UploadedFile inputfile = event.getFile();
        image.setImageByte(inputfile.getContent());
        image.setAuthor("test");
        imageService.saveImage(image);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", image.getId() + " is uploaded."));
      */
    }
}
