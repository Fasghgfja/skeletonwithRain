package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.faces.FacesException;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CaptureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Getter
@Setter
@Component
@Scope("view")
public class PhotoCamController implements Serializable {

    @Autowired
    ImageService imageService;


    private String filename;


    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    public String getFilename() {
        return filename;
    }

    //TODO:change this so that on capture it only shows the preview (similar as the preview of fileupload , and only saves the picture after imput of save (happy with the picture))
    public void oncapture(CaptureEvent captureEvent) {
        byte[] data = captureEvent.getData();
        Image image = new at.qe.skeleton.model.Image();
        image.setId(50100L);
        image.setImageByte(data);
        imageService.saveImage(image);
    }


    public ByteArrayInputStream getLastPicture() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String id = facesContext.getExternalContext().getInitParameterMap().get("id");
        System.out.println("IM get last picture and id is" + id);
        if (id == null) {
            System.err.println("id = " + id);
            Image image = imageService.loadImage(1L);
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        } else {
            Image image = imageService.loadImage(50100L);
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        }
    }



}