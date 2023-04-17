package at.qe.skeleton.ui.controllers;


import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;



/**
 * InputNumberController
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@Named("qrCodeController")
@ViewScoped
public class QRCodeController implements Serializable {

    private static final long serialVersionUID = 20120316L;
    private String renderMethod;
    private String text;
    private String label;
    private int mode;
    private int size;
    private String fillColor;

    public QRCodeController() {
        renderMethod = "canvas";
        text = "http://primefaces-extensions.github.io/";
        label = "PF-Extensions";
        mode = 2;
        fillColor = "7d767d";
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

}
