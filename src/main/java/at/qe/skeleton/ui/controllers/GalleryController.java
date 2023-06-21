package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Named;
import org.primefaces.model.ResponsiveOption;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Basic Session scoped bean for Dynamic content rendering / streaming
 * the bean returns a ByteArrayimput stream which is used in media.xhtml to reproduce images
 * Per default JSF only supports static content placed in resource folders. Streaming of dynamic content can be done with a custom Servlet or with PrimeFaces:
 * What happens when rendering the p:graphicImage:
 * The ValueExpression will be resolved: #{imageView.image} (StreamedContent.class) is extraced via EL API
 * The ValueExpression as string (#{imageView.image}) and the value type (StreamedContent.class) will be extracted
 * ImageView and therefore DefaultStreamedContent might get instantiated here, when the EL API can't resolve the value type (StreamedContent.class) correctly
 * a UID is generated
 * the UID and the ValueExpression string are stored into the HTTP session
 * the UID is appended to the image URL, which points to JSF ResourceHandler
 * What happens when the browser requests the URL:
 * our ResourceHandler gets the UID from the URL
 * receive the ValueExpression from the session
 * call the ValueExpression via EL API
 * ImageView and therefore DefaultStreamedContent is instantiated
 * the stream from the StreamedContent is now copied to the HTTP response
 * As the resource is streamed in a second request, which is not bound to any viewstate, @ViewScoped beans are not supported.
 * A common pattern is to pass the information, which is probably stored in your @ViewScoped bean, via request parameters.
 */



@Component
@Named
@Scope("application")
public class GalleryController implements Serializable {

    private static final String IMAGE_APPROVED = "IMAGE APPROVED: ";
    private static final String LARGE = "1024px";
    private static final String MEDIUM = "768px";
    private static final String SMALL = "560px";

    @Autowired
    private transient ImageService imageService;

    @Autowired
    private transient SessionInfoBean sessionInfoBean;

    @Autowired
    private transient LogRepository logRepository;

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GalleryController.class);

    private List<Image> images;
    private List<Image> approvedimages;

    private List<ResponsiveOption> responsiveOptions1;

    private List<ResponsiveOption> responsiveOptions2;

    private List<ResponsiveOption> responsiveOptions3;

    private int activeIndex = 0;

    /**
     *  This method initializes the {@link #responsiveOptions1} list of {@link ResponsiveOption} objects for the gallery {@code p:galleria}, using PrimeFaces responsive options.
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
        images = imageService.getAllImages();
        responsiveOptions1 = new ArrayList<>();
        responsiveOptions1.add(new ResponsiveOption(LARGE, 5));
        responsiveOptions1.add(new ResponsiveOption(MEDIUM, 3));
        responsiveOptions1.add(new ResponsiveOption(SMALL, 1));

        responsiveOptions2 = new ArrayList<>();
        responsiveOptions2.add(new ResponsiveOption(LARGE, 5));
        responsiveOptions2.add(new ResponsiveOption("960px", 4));
        responsiveOptions2.add(new ResponsiveOption(MEDIUM, 3));
        responsiveOptions2.add(new ResponsiveOption(SMALL, 1));

        responsiveOptions3 = new ArrayList<>();
        responsiveOptions3.add(new ResponsiveOption("1500px", 15));
        responsiveOptions3.add(new ResponsiveOption(LARGE, 15));
        responsiveOptions3.add(new ResponsiveOption(MEDIUM, 15));
        responsiveOptions3.add(new ResponsiveOption(SMALL, 15));
    }



    public void changeActiveIndex() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.activeIndex = Integer.valueOf(params.get("index"));
    }


    public List<ResponsiveOption> getResponsiveOptions1() {
        return responsiveOptions1;
    }

    public List<ResponsiveOption> getResponsiveOptions2() {
        return responsiveOptions2;
    }

    public List<ResponsiveOption> getResponsiveOptions3() {
        return responsiveOptions3;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public void setService(ImageService service) {
        this.imageService = service;
    }


    /**
     * Returns a list of all images available in the application.
     * @return a list of Image objects representing all images in the application.
     */
    public List<Image> getImages() {
        return imageService.getAllImages();
    }

    /**
     * This method returns an instance of ByteArrayInputStream for the image content requested.
     * It retrieves the image ID from the request parameters and loads the corresponding image using {@link ImageService#loadImage(Long)}.
     * The method returns the image content as a ByteArrayInputStream.
     * @return An instance of ByteArrayInputStream containing the requested image content.
     */
    public ByteArrayInputStream getPhotoAsStreamedContent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            byte []a = new byte[0];
            return new ByteArrayInputStream(a);
        } else {
            String imageId = context.getExternalContext().getRequestParameterMap().get("id");
            Image image = imageService.loadImage(Long.valueOf(imageId));
            byte[] imageBytes = image.getImageByte();
            return new ByteArrayInputStream(imageBytes);
        }
    }

    /**
     * Method to get the profile picture of the user as a streamed content.
     * @param id id of the profile picture as string
     * @return ByteArrayInputStream for the profile picture.
     */

    public ByteArrayInputStream getProfilePicAsStreamedContent(String id) {
        if (id == null) {
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
     * Get all images of a plant.
     * @param idString id as a string of the plant that we want to get the images of.
     * @return List of images that are linked to the plant.
     */

    public List<Image> doGetPlantImages(String idString) {
        if (idString == null){return new ArrayList<>();}
        return imageService.getAllPlantImages(idString);
    }

    /**
     * Gets all images that are yet to be approved by a gardener or an admin.
     * @param idString String of the id of the plant.
     * @return List of images that have not been approved yet for a certain plant.
     */

    public List<Image> doGetPlantImagesNotYetApproved(String idString) {
        if (idString == null){return new ArrayList<>();}
        return imageService.getAllPlantImagesNotYetApproved(idString);
    }

    /**
     * Method to get all approved images of a certain plant. These will be displayed in the gallery of the plant.
     * @param idString Id of the plant as a string.
     * @return List of approved images for a certain plant.
     */
    public List<Image> doGetApprovedPlantImages(String idString) {
        if (idString == null){return new ArrayList<>();}
        return imageService.getAllApprovedPlantImages(idString);
    }

    /**
     * Gets all approved images for all plants.
     * @return List of all images that have already been approved for any plant.
     */
    public List<Image> getApprovedImages() {
        return imageService.getApprovedImages();
    }

    /**
     * Gets all images not yet approved for every plant.
     * @return List of all images that have not been approved yet.
     */

    public List<Image> getNotApprovedImages() {
        return imageService.getNotApprovedImages();
    }

    /**
     * Method to get all approved images of all plants
     * this is used in register.xhtml through the planthphotogallerycontroller (view) when it is loaded without opening
     * a specific plantid from a qr code and it makes possible
     * to see a gallery containing all approved plant images in the system.
     * */
    public List<Image> doGetAllApprovedPlantImages() {
        return imageService.doGetAllApprovedPlantImages();
    }

    /**
     * Delete an image.
     * @param image to be deleted.
     */

    public void doDeleteImage(Image image) {
        imageService.deleteImage(image);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info("IMAGE DELETED: " + image.getId());
            successFileHandler.close();
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("IMAGE DELETION");
        createLog.setText(IMAGE_APPROVED + image.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }

    /**
     * Approve an image. Gardeners that are assigned to a sensor station and all admins need to approve images before they are displayed in
     * the corresponding plant gallery.
     * @param image Image to be approved.
     */

    public void approveImage(Image image) {
        image.setApproved(true);
        imageService.saveImage(image);
        try {
            successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
            successFileHandler.setFormatter(new SimpleFormatter());
            successLogger.addHandler(successFileHandler);
            successLogger.info(IMAGE_APPROVED + image.getId());
            successFileHandler.close();
        } catch (IOException e) {
            LOGGER.error("error", e);
        }
        Log createLog = new Log();
        createLog.setDate(LocalDate.now());
        createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        createLog.setAuthor(sessionInfoBean.getCurrentUserName());
        createLog.setSubject("IMAGE APPROVED");
        createLog.setText(IMAGE_APPROVED + image.getId());
        createLog.setType(LogType.SUCCESS);
        logRepository.save(createLog);
    }

    /**
     * Gets the total amount of images stored in the database.
     * @return Integer showing the amount of total images.
     */

    public Integer getTotalImagesAmount() {
        return imageService.getTotalImagesAmount();
    }

    /**
     * Gets the total amount of images stored in the database that are approved.
     * @return Integer showing the amount of total images approved.
     */
    public Integer getApprovedImagesAmount() {
        return imageService.getApprovedImagesAmount();
    }

    /**
     * Gets the total amount of images stored in the database that are not approved yet.
     * @return Integer showing the amount of total images not approved yet.
     */
    public Integer getNotApprovedImagesAmount() {
        return imageService.getNotApprovedImagesAmount();
    }

    /**
     * Gets the total amount of images stored in the database that are not linked to a plant.
     * @return Integer showing the amount of total images that are not linked to a plant.
     */
    public Integer getApprovedImagesNoPlantAmount() {
        return imageService.getApprovedImagesNoPlantAmount();
    }


    public void setImageService(ImageService imageService) {
    }

    public void setSessionInfoBean(SessionInfoBean sessionInfoBean) {
    }

    public void setLogRepository(LogRepository logRepository) {
    }


}

