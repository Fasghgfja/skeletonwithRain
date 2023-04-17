package at.qe.skeleton.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents guest preferences for the UI theme, color schemes, menu and profile modes, and other settings.
 * It is a session-scoped managed bean and can be injected into other beans or JSF views.
 * if a layout variable is changed then the nexessary theme css has to be included in the css folder.
 */
@Component
@Scope("session")
public class GuestPreferences implements Serializable {

    private String menuMode = "layout-horizontal layout-static-active";
    private String darkMode = "light";
    private String layoutPrimaryColor = "chateau-green";
    private String componentTheme = "chateau-green";
    private String topbarTheme = "colored";
    private String menuTheme = "dim";
    private String profileMode = "popup";
    private String inputStyle = "outlined";
    private boolean lightLogo = true;


    /**
     * Sets the current dark mode and updates the menu and topbar themes accordingly.
     * If the topbar theme is set to "light", the logo is set to dark mode; otherwise, it is set to light mode.
     */
    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
        this.menuTheme = darkMode;
        this.topbarTheme = darkMode;
        this.lightLogo = !this.topbarTheme.equals("light");
    }

    /**
     * Returns the CSS class for the current layout.
     */
    public String getLayout() {
        return "layout-" + this.layoutPrimaryColor + '-' + this.darkMode ;
    }

    public boolean isLightLogo() {
        return lightLogo;
    }
    public String getLayoutPrimaryColor() {
        return layoutPrimaryColor;
    }
    public String getDarkMode() {
        return darkMode;
    }
    public String getComponentTheme() {
        return componentTheme;
    }
    public String getMenuTheme() {
        return menuTheme;
    }
    public String getTopbarTheme() {
        return topbarTheme;
    }
    public String getMenuMode() {
        return this.menuMode;
    }
    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
    }
    public String getProfileMode() {
        return this.profileMode;
    }
    public void setProfileMode(String profileMode) {
        this.profileMode = profileMode;
    }
    public String getInputStyle() {
        return inputStyle;
    }
    public void setInputStyle(String inputStyle) {
        this.inputStyle = inputStyle;
    }
    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }
    public List<LayoutPrimaryColor> getLayoutPrimaryColors() {
        return layoutPrimaryColors;
    }



    /**
     * A list of available layout primary colors.
     */
    private List<LayoutPrimaryColor> layoutPrimaryColors = new ArrayList<>();

    /**
     This class represents a primary color that can be used for layout themes.
     */
    public class LayoutPrimaryColor {
        String name;
        String file;
        String color;

        /**
        * Constructs a new LayoutPrimaryColor object.
        * @param name the name of the primary color
        * @param file the name of the file associated with the primary color
        * @param color the hexadecimal code for the primary color
         */
        public LayoutPrimaryColor(String name, String file, String color) {
            this.name = name;
            this.file = file;
            this.color = color;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

        public String getColor() {
            return this.color;
        }
    }


}
