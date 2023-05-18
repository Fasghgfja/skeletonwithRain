package at.qe.skeleton.ui.beans;

import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@SessionScoped
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

    private List<ComponentTheme> componentThemes = new ArrayList<ComponentTheme>();

    private List<LayoutPrimaryColor> layoutPrimaryColors = new ArrayList<LayoutPrimaryColor>();

    @PostConstruct
    public void init() {
        componentThemes.add(new ComponentTheme("Chateau Green", "chateau-green", "#3C9462"));

        layoutPrimaryColors.add(new LayoutPrimaryColor("Chateau Green", "chateau-green", "#3C9462"));

    }

    public String getDarkMode() {
        return darkMode;
    }

    public boolean isLightLogo() {
        return lightLogo;
    }


        public void setDarkMode(String darkMode) {
            System.out.println("im setting darkmode");
            this.darkMode = darkMode;
            this.menuTheme = "dim";
            this.topbarTheme = "colored";
            this.lightLogo = !this.topbarTheme.equals("light");
        }

    public String getLayout() {
        return "layout-" + this.layoutPrimaryColor + '-' + this.darkMode ;
    }

    public String getTheme() {
        return this.componentTheme + '-' + this.darkMode ;
    }

    public String getLayoutPrimaryColor() {
        return layoutPrimaryColor;
    }

    public void setLayoutPrimaryColor(String layoutPrimaryColor) {
        this.layoutPrimaryColor = layoutPrimaryColor;
        this.componentTheme = layoutPrimaryColor;
    }

    public String getComponentTheme() {
        return componentTheme;
    }

    public void setComponentTheme(String componentTheme) {
        this.componentTheme = componentTheme;
    }

    public String getMenuTheme() {
        return menuTheme;
    }

    public void setMenuTheme(String menuTheme) {
        this.menuTheme = menuTheme;
    }

    public String getTopbarTheme() {
        return topbarTheme;
    }

    public void setTopbarTheme(String topbarTheme) {
        this.topbarTheme = topbarTheme;
        this.lightLogo = !this.topbarTheme.equals("light");
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


    public List<ComponentTheme> getComponentThemes() {
        return componentThemes;
    }

    public class ComponentTheme {
        String name;
        String file;
        String color;

        public ComponentTheme(String name, String file, String color) {
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

    public List<LayoutPrimaryColor> getLayoutPrimaryColors() {
        return layoutPrimaryColors;
    }

    public class LayoutPrimaryColor {
        String name;
        String file;
        String color;

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
