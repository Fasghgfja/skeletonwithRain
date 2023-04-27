package at.qe.skeleton.api.controllers;

import at.qe.skeleton.model.Image;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import at.qe.skeleton.services.ImageService;


import java.io.IOException;
import java.io.InputStream;


@RestController
public class FileUploadControllerRest {



    @Autowired
    private ImageService imageService;


//TODO Fix page id  cannot be null from xhtml to have fileupload with no id aswell
    @PostMapping(value = "api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView uploadFile(@RequestParam MultipartFile file, ModelMap modelMap, @RequestParam("id") String id) throws IOException {
        modelMap.addAttribute("file", file);

        System.out.println("RestFileupload");
        Image image = new at.qe.skeleton.model.Image();
        image.setImageByte(file.getBytes());

        if(!id.equals("null")){
            imageService.addPictureToPlantPictures(image,id);
            String redirectUrl = "/file.xhtml?id=" + id;
            return new RedirectView(redirectUrl);
        }

        imageService.saveImage(image);

        String redirectUrl = "/file.xhtml";
        return new RedirectView(redirectUrl);
    }





}


