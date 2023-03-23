package at.qe.skeleton.ui.beans;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Basic Session scoped bean to show images for a image gallery
 * the bean returns a ByteArrayimput stream which is used in media.xhtml to reproduce images
 *
 * check if request scope is better or session
 */

//TODO: pass real id to show more images instal of 1L only


@Component
@Scope("request")
public class GalleryController implements Serializable {

    @Autowired
    private ImageService imageService;


    public ByteArrayInputStream getPhotoAsStreamedContent() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String id = facesContext.getExternalContext().getRequestParameterMap().get("id");
        Image image = imageService.loadImage(1L);
        byte[] imageBytes = image.getImageByte();
        return new ByteArrayInputStream(imageBytes);
    }


    public List<Image> getImages() {
        return imageService.getAllImages();
    }



}

