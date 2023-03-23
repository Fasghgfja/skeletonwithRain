package at.qe.skeleton.ui.beans;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import org.primefaces.model.ResponsiveOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic Session scoped bean for Dynamic content rendering / streaming
 * the bean returns a ByteArrayimput stream which is used in media.xhtml to reproduce images
 *
 * Per default JSF only supports static content placed in resource folders. Streaming of dynamic content can be done with a custom Servlet or with PrimeFaces:
 * What happens when rendering the p:graphicImage:
 * The ValueExpression will be resolved: #{imageView.image} (StreamedContent.class) is extraced via EL API
 * The ValueExpression as string (#{imageView.image}) and the value type (StreamedContent.class) will be extracted
 * ImageView and therefore DefaultStreamedContent might get instantiated here, when the EL API can't resolve the value type (StreamedContent.class) correctly
 * a UID is generated
 * the UID and the ValueExpression string are stored into the HTTP session
 * the UID is appended to the image URL, which points to JSF ResourceHandler
 *
 * What happens when the browser requests the URL:
 * our ResourceHandler gets the UID from the URL
 * receive the ValueExpression from the session
 * call the ValueExpression via EL API
 * ImageView and therefore DefaultStreamedContent is instantiated
 * the stream from the StreamedContent is now copied to the HTTP response
 *
 * As the resource is streamed in a second request, which is not bound to any viewstate, @ViewScoped beans are not supported.
 * A common pattern is to pass the information, which is probably stored in your @ViewScoped bean, via request parameters.
 *
 */

//TODO: pass real id to show more images instal of 1L only


@Component
@Scope("request")
public class GalleryController implements Serializable {

    @Autowired
    private ImageService imageService;

    private List<ResponsiveOption> responsiveOptions;


    @PostConstruct
    public void init() {
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
    }


    public ByteArrayInputStream getPhotoAsStreamedContent() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String id = facesContext.getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            System.err.println("id = " + id);
            Image image = imageService.loadImage(1L);
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        } else {
            Image image = imageService.loadImage(Long.valueOf(id));
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        }
    }

    public List<Image> getImages() {
        return imageService.getAllImages();
    }



}

