package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.Plant;
import at.qe.skeleton.services.PlantService;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Component
@Scope("view")
public class QrCodeController implements Serializable {

    private static final String PLANT_NOT_FOUND = "Plant not Found";
    @Autowired
    private transient PlantService plantService;

    private Long id;
    private static final long serialVersionUID = 20120316L;
    private String renderMethod;
    private String text;
    private String label;
    private int mode;
    private int size;
    private String fillColor;

    private String linkInput;

    public QrCodeController() {
        renderMethod = "canvas";
        text = "http://localhost:8080/registration/register.xhtml?id=";
        label = "/register.xhtml?id=";
        mode = 2;
        fillColor = "8d888d";
        size = 200;
    }

    public void setText(final String text) {
        if (this.id != 0) {
            return;
        }
        this.text = "http://localhost:8080/registration/register.xhtml?id=" + text;
        if (!(text.length() > 0 && text.length() <= 19) || !(text.matches("\\d+"))) {
            this.label = PLANT_NOT_FOUND;
            return;
        }
        Plant lookingfor = plantService.loadPlant(Long.parseLong(text));
        if (lookingfor == null) {
            this.label = PLANT_NOT_FOUND;
        } else {
            this.label = lookingfor.getPlantName();
        }
    }


    public void init() {
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        if (params.get("id") == null) {
            return;
        }
        this.id = Long.parseLong(params.get("id"));
        text = text + id;

        Plant lookingfor = plantService.loadPlant(Long.parseLong(id.toString()));
        if (lookingfor == null) {
            this.label = PLANT_NOT_FOUND;
        } else {
            this.label = lookingfor.getPlantName();
        }
    }
}