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

}
