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

    private byte[] previewImage = null;


    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    public byte[] getPreviewImage() {
        return previewImage;
    }

    //TODO:change this so that on capture it only shows the preview (similar as the preview of fileupload , and only saves the picture after imput of save (happy with the picture))
    public void oncapture(CaptureEvent captureEvent) {
        byte[] data = captureEvent.getData();
        System.out.println("Photocamcontroller: Im ON capture and im savbing a picture"); //debug print
        previewImage = data;
        Image image = new at.qe.skeleton.model.Image();
        image.setImageByte(data);
        imageService.saveImage(image);
    }

}