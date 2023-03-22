package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.Image;
import at.qe.skeleton.services.ImageService;
import jakarta.annotation.PostConstruct;
import org.primefaces.model.ResponsiveOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class GalleryController {


    private List<ResponsiveOption> responsiveOptions;

    private List<Image> images;

    @Autowired
    private ImageService imageService;

    @PostConstruct
    public void init() {
        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));

        images = imageService.getAllImages();
    }



    /**
     * Method made for testing
     * */
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }

    public List<Image> getImages() {
        images = imageService.getAllImages();

        return images;
    }

}

