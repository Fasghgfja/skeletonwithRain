package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Image;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.services.ImageService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    private FileUploadController fileUploadController;

    @Mock
    private ImageService imageService;

    @Mock
    private SessionInfoBean sessionInfoBean;

    @Mock
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileUploadController = new FileUploadController();
        fileUploadController.setImageService(imageService);
        fileUploadController.setSessionInfoBean(sessionInfoBean);
        fileUploadController.setLogRepository(logRepository);
    }

    @Test
    void testHandleFileUpload() throws IOException {
        FileUploadEvent event = mock(FileUploadEvent.class);
        FacesContext facesContext = mock(FacesContext.class);
        ExternalContext externalContext = mock(ExternalContext.class);
        UploadedFile uploadedFile = mock(UploadedFile.class);
        BufferedImage inputImage = mock(BufferedImage.class);
        BufferedImage outputImage = mock(BufferedImage.class);
        ByteArrayOutputStream out = mock(ByteArrayOutputStream.class);
        Image image = new Image();
        Log createLog = new Log();

        when(event.getFile()).thenReturn(uploadedFile);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getRequestParameterMap()).thenReturn(Collections.singletonMap("id", "1"));
        when(uploadedFile.getFileName()).thenReturn("test.jpg");
        when(uploadedFile.getInputStream()).thenReturn(mock(InputStream.class));
        when(ImageIO.read(any(InputStream.class))).thenReturn(inputImage);
        when(inputImage.getScaledInstance(anyInt(), anyInt(), anyInt())).thenReturn(mock(java.awt.Image.class));
        when(outputImage.createGraphics()).thenReturn(mock(Graphics2D.class));
        when(out.toByteArray()).thenReturn(new byte[0]);
        doNothing().when(logRepository).save(any(Log.class));

        fileUploadController.handleFileUpload(event);

        verify(facesContext, times(1)).addMessage(null, new FacesMessage("Success! ", "test.jpg is uploaded."));
        verify(imageService, times(1)).addPictureToPlantPictures(any(Image.class), eq("1"));
        verify(logRepository, times(1)).save(any(Log.class));
    }
}
