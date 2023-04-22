package at.qe.skeleton.ui.controllers;



import java.io.IOException;
import java.io.Serializable;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.codescanner.Code;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@Named
@ViewScoped
public class CodeScannerController implements Serializable {

    public void onCodeScanned(final SelectEvent<Code> event) {
        final Code code = event.getObject();
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        String.format("Scanned: %s (%s)", code.getValue(), code.getFormat()),
                        null));
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(code.getValue());
        } catch (IOException e) {
            // handle the exception
        }
    }

}
