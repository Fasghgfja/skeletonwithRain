package at.qe.skeleton.ui.beans;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@RequestScoped
public class YamlDownloadBean {
    private StreamedContent file;
    public YamlDownloadBean() {
        file = DefaultStreamedContent.builder()
                .name("config.yaml")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/config.yaml"))
                .build();
    }

    public StreamedContent getFile() {
        return file;
    }
}