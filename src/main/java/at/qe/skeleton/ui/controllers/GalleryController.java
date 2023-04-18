package at.qe.skeleton.ui.controllers;


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
@Component
@Scope("request")
public class GalleryController implements Serializable {

    @Autowired
    private ImageService imageService;

    private List<ResponsiveOption> responsiveOptions;

    //TODO: use primefaces responsive options for the gallery
    /**
     *  This method initializes the {@link #responsiveOptions} list of {@link ResponsiveOption} objects for the gallery {@code p:galleria}, using PrimeFaces responsive options.
     *  <p>The method sets up different configurations of {@link ResponsiveOption} objects for different screen sizes,
     *  @see <a href="https://primefaces.github.io/primefaces/10_0_0/#/components/galleria?id=resposive-options">p:galleria responsive options</a>
     *  as a  {@code @PostConstruct}. annotated method is called after the bean has been constructed and all its dependencies have been injected.
     *  so that the gallery can adjust itself responsively to the device's screen width.
     *  The responsive configurations are based on the recommended responsive options by PrimeFaces for Galleria component.
     *  @see <a href="https://www.primefaces.org/showcase/ui/data/galleria/basic.xhtml">PrimeFaces Galleria component documentation</a>
     *  @see ResponsiveOption
     */
    @PostConstruct
    public void init() {
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
    }

    /**
     * This method returns an instance of ByteArrayInputStream for the image content requested.
     * It retrieves the image ID from the request parameters and loads the corresponding image using {@link ImageService#loadImage(Long)}.
     * If no image ID is found in the request parameters, it loads a default image using {@link ImageService#loadImage(Long)} with an ID of 1L.
     * THIS NEEDS TO BE RESOLVED
     * The method returns the image content as a ByteArrayInputStream.
     * @return An instance of ByteArrayInputStream containing the requested image content.
     */
    //TODO: multiple null requests bug: when the gallery page is opened it appears multiple requests are made so some carry null while usually the last one carry a real value
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




    public ByteArrayInputStream getProfilePicAsStreamedContent(String id) {
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









    /**
     * Returns a list of all images available in the application.
     * @return a list of Image objects representing all images in the application.
     */
    public List<Image> getImages() {
        return imageService.getAllImages();
    }



}

