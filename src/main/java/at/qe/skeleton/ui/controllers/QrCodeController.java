package at.qe.skeleton.ui.controllers;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class QrCodeController implements Serializable {

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
        text = "http://localhost:8080/registration/register.xhtml?id=50100";
        label = "/register.xhtml?id=50100";
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

    public void setText(final String text) {
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

}