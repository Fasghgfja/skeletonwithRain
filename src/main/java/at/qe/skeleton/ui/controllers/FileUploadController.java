package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@RequestScoped
public class FileUploadController {

    private UploadedFile file;

    @Autowired
    private ImageService imageService;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }


    public void upload(Long plantId) {
        Image image = new Image(file.getFileName(), file.getContent());
        imageService.saveImage(image, plantId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", file.getFileName() + " is uploaded."));
    }
}
