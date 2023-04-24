package at.qe.skeleton.ui.controllers;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.CaptureEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Getter
@Setter
@Component
@Scope("view")
public class PhotoCamController implements Serializable {

    private String filename;


    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    public String getFilename() {
        return filename;
    }

    public void oncapture(CaptureEvent captureEvent) {
        filename = getRandomImageName();
        byte[] data = captureEvent.getData();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo"
                + File.separator + "images" + File.separator + "photocam" + File.separator + filename + ".jpeg";

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch (IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }
}