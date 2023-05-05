package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.model.Plant;
import at.qe.skeleton.services.PlantService;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
@Scope("view")
public class QrCodeController implements Serializable {

    @Autowired
    PlantService plantService;

    private Long id;
    private static final long serialVersionUID = 20120316L;
    private String renderMethod;
    private String text;
    private String label;
    private int mode;
    private int size;
    private String fillColor;

    private String linkInput; // Add this field to hold the new link input



    public QrCodeController() {
        renderMethod = "canvas";
        text = "http://localhost:8080/registration/register.xhtml?id=";
        label = "/register.xhtml?id=";
        mode = 2;
        fillColor = "8d888d";
        size = 200;
    }

    public String getRenderMethod() {
        return renderMethod;
    }

    public void setRenderMethod(final String renderMethod) {
        this.renderMethod = renderMethod;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) { //TODO : if resources become a thing this is unnecessary caching for a stupid thing : )
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(final int mode) {
        this.mode = mode;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(final String fillColor) {
        this.fillColor = fillColor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }



    public String getLinkInput() {
        return linkInput;
    }

    public void setLinkInput(final String linkInput) {
        this.linkInput = linkInput;
    }

    public void init() {
        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        this.id = Long.parseLong(params.get("id"));
        text = text+id;
        label = label +id;
    }
}