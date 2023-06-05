package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.model.ResponsiveOption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GalleryControllerTest {

    private GalleryController galleryController;

    @Mock
    private ImageService imageService;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @Mock
    private LogRepository logRepository;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        galleryController = new GalleryController();
        galleryController.setImageService(imageService);
        galleryController.setSessionInfoBean(sessionInfoBean);
        galleryController.setLogRepository(logRepository);
        galleryController.init();
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
    }

    @Test
    void testChangeActiveIndex() {
        Map<String, String> requestParameterMap = mock(Map.class);
        when(externalContext.getRequestParameterMap()).thenReturn(requestParameterMap);
        when(requestParameterMap.get("index")).thenReturn("1");

        galleryController.changeActiveIndex();

        assertEquals(1, galleryController.getActiveIndex());
    }


    @Test
    void testGetActiveIndex() {
        galleryController.setActiveIndex(2);

        assertEquals(2, galleryController.getActiveIndex());
    }

    @Test
    void testSetActiveIndex() {
        galleryController.setActiveIndex(3);

        assertEquals(3, galleryController.getActiveIndex());
    }

    @Test
    void testGetImages() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getAllImages()).thenReturn(expectedImages);

        List<Image> images = galleryController.getImages();

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getAllImages();
    }

    @Test
    void testGetPhotoAsStreamedContent_RenderResponsePhase() {
        when(facesContext.getCurrentPhaseId()).thenReturn(PhaseId.RENDER_RESPONSE);

        ByteArrayInputStream stream = galleryController.getPhotoAsStreamedContent();

        assertNotNull(stream);
        assertEquals(0, stream.available());
    }

    @Test
    void testGetPhotoAsStreamedContent_NonRenderResponsePhase() {
        when(facesContext.getCurrentPhaseId()).thenReturn(PhaseId.INVOKE_APPLICATION);
        when(externalContext.getRequestParameterMap()).thenReturn(Collections.singletonMap("id", "1"));
        Image image = new Image();
        image.setImageByte(new byte[0]);
        when(imageService.loadImage(1L)).thenReturn(image);

        ByteArrayInputStream stream = galleryController.getPhotoAsStreamedContent();

        assertNotNull(stream);
        assertEquals(0, stream.available());
        verify(imageService, times(1)).loadImage(1L);
    }

    @Test
    void testGetProfilePicAsStreamedContent_WithId() {
        Image image = new Image();
        image.setImageByte(new byte[0]);
        when(imageService.loadImage(2L)).thenReturn(image);

        ByteArrayInputStream stream = galleryController.getProfilePicAsStreamedContent("2");

        assertNotNull(stream);
        assertEquals(0, stream.available());
        verify(imageService, times(1)).loadImage(2L);
    }

    @Test
    void testGetProfilePicAsStreamedContent_NullId() {
        Image image = new Image();
        image.setImageByte(new byte[0]);
        when(imageService.loadImage(1L)).thenReturn(image);

        ByteArrayInputStream stream = galleryController.getProfilePicAsStreamedContent(null);

        assertNotNull(stream);
        assertEquals(0, stream.available());
        verify(imageService, times(1)).loadImage(1L);
    }

    @Test
    void testDoGetPlantImages_NullIdString() {
        List<Image> images = galleryController.doGetPlantImages(null);

        assertNotNull(images);
        assertTrue(images.isEmpty());
    }

    @Test
    void testDoGetPlantImages_NotNullIdString() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getAllPlantImages("1")).thenReturn(expectedImages);

        List<Image> images = galleryController.doGetPlantImages("1");

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getAllPlantImages("1");
    }

    @Test
    void testDoGetPlantImagesNotYetApproved_NullIdString() {
        List<Image> images = galleryController.doGetPlantImagesNotYetApproved(null);

        assertNotNull(images);
        assertTrue(images.isEmpty());
    }

    @Test
    void testDoGetPlantImagesNotYetApproved_NotNullIdString() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getAllPlantImagesNotYetApproved("1")).thenReturn(expectedImages);

        List<Image> images = galleryController.doGetPlantImagesNotYetApproved("1");

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getAllPlantImagesNotYetApproved("1");
    }

    @Test
    void testDoGetApprovedPlantImages_NullIdString() {
        List<Image> images = galleryController.doGetApprovedPlantImages(null);

        assertNotNull(images);
        assertTrue(images.isEmpty());
    }

    @Test
    void testDoGetApprovedPlantImages_NotNullIdString() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getAllApprovedPlantImages("1")).thenReturn(expectedImages);

        List<Image> images = galleryController.doGetApprovedPlantImages("1");

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getAllApprovedPlantImages("1");
    }

    @Test
    void testGetApprovedImages() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getApprovedImages()).thenReturn(expectedImages);

        List<Image> images = galleryController.getApprovedImages();

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getApprovedImages();
    }

    @Test
    void testGetNotApprovedImages() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.getNotApprovedImages()).thenReturn(expectedImages);

        List<Image> images = galleryController.getNotApprovedImages();

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).getNotApprovedImages();
    }

    @Test
    void testDoGetAllApprovedPlantImages() {
        List<Image> expectedImages = new ArrayList<>();
        when(imageService.doGetAllApprovedPlantImages()).thenReturn(expectedImages);

        List<Image> images = galleryController.doGetAllApprovedPlantImages();

        assertEquals(expectedImages, images);
        verify(imageService, times(1)).doGetAllApprovedPlantImages();
    }

    @Test
    void testDoDeleteImage() {
        Image image = new Image();
        galleryController.doDeleteImage(image);

        verify(imageService, times(1)).deleteImage(image);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testApproveImage() {
        Image image = new Image();
        galleryController.approveImage(image);

        assertTrue(image.isApproved());
        verify(imageService, times(1)).saveImage(image);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testGetTotalImagesAmount() {
        when(imageService.getTotalImagesAmount()).thenReturn(10);

        Integer amount = galleryController.getTotalImagesAmount();

        assertEquals(10, amount);
        verify(imageService, times(1)).getTotalImagesAmount();
    }

    @Test
    void testGetApprovedImagesAmount() {
        when(imageService.getApprovedImagesAmount()).thenReturn(5);

        Integer amount = galleryController.getApprovedImagesAmount();

        assertEquals(5, amount);
        verify(imageService, times(1)).getApprovedImagesAmount();
    }

    @Test
    void testGetNotApprovedImagesAmount() {
        when(imageService.getNotApprovedImagesAmount()).thenReturn(3);

        Integer amount = galleryController.getNotApprovedImagesAmount();

        assertEquals(3, amount);
        verify(imageService, times(1)).getNotApprovedImagesAmount();
    }

    @Test
    void testGetApprovedImagesNoPlantAmount() {
        when(imageService.getApprovedImagesNoPlantAmount()).thenReturn(2);

        Integer amount = galleryController.getApprovedImagesNoPlantAmount();

        assertEquals(2, amount);
        verify(imageService, times(1)).getApprovedImagesNoPlantAmount();
    }
}
