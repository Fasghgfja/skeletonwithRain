package at.qe.skeleton.ui.beans;

import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to switch between dark mode and light mode.
 */
@Getter
@Setter
@SessionScoped
public class GuestPreferences implements Serializable {

    private static final String LIGHT = "light";
    private static final String CHATEAU_GREEN = "chateau-green";

    private String menuMode = "layout-horizontal layout-static-active";
    private String darkMode = LIGHT;
    private String layoutPrimaryColor = CHATEAU_GREEN;
    private String componentTheme = CHATEAU_GREEN;
    private String topbarTheme = "colored";
    private String menuTheme = "dim";
    private String profileMode = "popup";
    private String inputStyle = "outlined";
    private boolean lightLogo = true;

    private transient List<ComponentTheme> componentThemes = new ArrayList<>();

    private transient List<LayoutPrimaryColor> layoutPrimaryColors = new ArrayList<>();

    @PostConstruct
    public void init() {
        componentThemes.add(new ComponentTheme("Chateau Green", CHATEAU_GREEN, "#3C9462"));

        layoutPrimaryColors.add(new LayoutPrimaryColor("Chateau Green", CHATEAU_GREEN, "#3C9462"));

    }

    public boolean isLightLogo() {
        return lightLogo;
    }


    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
        this.menuTheme = "dim";
        this.topbarTheme = "colored";
        this.lightLogo = true;
    }

    public String getLayout() {
        return "layout-" + this.layoutPrimaryColor + '-' + this.darkMode;
    }

    public String getTheme() {
        return this.componentTheme + '-' + this.darkMode;
    }

    public void setLayoutPrimaryColor(String layoutPrimaryColor) {
        this.layoutPrimaryColor = layoutPrimaryColor;
        this.componentTheme = layoutPrimaryColor;
    }

    public void setTopbarTheme(String topbarTheme) {
        this.topbarTheme = topbarTheme;
        this.lightLogo = !this.topbarTheme.equals(LIGHT);
    }

    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }

    /**
     * Inner class to get the component theme.
     */
    @Getter
    @Setter
    public class ComponentTheme {
        String name;
        String file;
        String color;

        public ComponentTheme(String name, String file, String color) {
            this.name = name;
            this.file = file;
            this.color = color;
        }
    }

    public List<LayoutPrimaryColor> getLayoutPrimaryColors() {
        return layoutPrimaryColors;
    }

    /**
     * Inner class to get the Layout primary color.
     */
    @Getter
    @Setter
    public class LayoutPrimaryColor {
        String name;
        String file;
        String color;

        public LayoutPrimaryColor(String name, String file, String color) {
            this.name = name;
            this.file = file;
            this.color = color;
        }
    }

}
