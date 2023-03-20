package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This beans provides functionality to register a new user.
 */
@Component
@SessionScoped
public class UserRegistrationBean implements Serializable {


    @Autowired
    private transient UserService userService;
    @Autowired
    private transient PasswordEncoder passwordEncoder;


    private String userName;
    private String password;
    private LocalDate dob;
    private String email;

    private List<String> selectedRoles = new ArrayList<>();
    private Set<UserRole> roles = new HashSet<>();


    public UserRegistrationBean() {
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void validateEmail(FacesContext context, UIComponent toValidate,
                              Object value) throws ValidatorException {
        String emailStr = (String) value;
        if (-1 == emailStr.indexOf("@")) {
            FacesMessage message = new FacesMessage("Invalid email address");
            throw new ValidatorException(message);
        }
    }

    public String addConfirmedUser() {
        createNewUser();
        boolean added = true; // actual application may fail to add user
        FacesMessage doneMessage = null;
        String outcome = null;
        if (added) {
            doneMessage = new FacesMessage("Successfully added new user");
            outcome = "done";
        } else {
            doneMessage = new FacesMessage("Failed to add new user");
            outcome = "register";
        }
        FacesContext.getCurrentInstance().addMessage(null, doneMessage);
        return outcome;
    }


    public void createNewUser() {
        Userx user = new Userx();

        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode(password));
       // user.setEmail(email);
        user.setEnabled(true);
        roles.add(UserRole.USER);
        user.setRoles(roles);
        userService.saveUser(user);
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }







}