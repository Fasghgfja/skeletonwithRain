package at.qe.skeleton.ui.beans;



import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("session")
public class GuestPreferences implements Serializable {

    private String menuMode = "layout-horizontal layout-static-active";

    private String darkMode = "light";

    private String layoutPrimaryColor = "cyan";

    private String componentTheme = "cyan";
    
    private String topbarTheme = "colored";

    private String menuTheme = "dim";

    private String profileMode = "popup";

    private String inputStyle = "outlined";

    private boolean lightLogo = true;


    private List<LayoutPrimaryColor> layoutPrimaryColors = new ArrayList<>();



    @PostConstruct
    public void init() {

    }

    public String getDarkMode() {
        return darkMode;
    }

    public boolean isLightLogo() {
        return lightLogo;
    }

    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
        this.menuTheme = darkMode;
        this.topbarTheme = darkMode;
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
