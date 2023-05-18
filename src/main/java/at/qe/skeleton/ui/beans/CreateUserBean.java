package at.qe.skeleton.ui.beans;


import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.UserService;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class handles the creation of new users via the webapp. This can either be done from the user management by an admin
 * or from the registration page by any unregistered user.
 */
@Getter
@Setter
@Component
@Named
@Scope("application")
public class CreateUserBean implements Serializable {

    private static final String CREATE = "CREATED USER: ";

    @Autowired
    private transient UserService userService;
    @Autowired
    private transient PasswordEncoder passwordEncoder;

    @Autowired
    private transient UserxRepository userxRepository;

    @Autowired
    private transient LogRepository logRepository;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private List<String> selectedRoles = new ArrayList<>();
    private Set<UserRole> roles = new HashSet<>();

    private final transient Logger successLogger = Logger.getLogger("SuccessLogger");
    private transient FileHandler successFileHandler;
    private final transient Logger warningOrErrorLogger = Logger.getLogger("WarningOrErrorLogger");
    private transient FileHandler warningOrErrorFileHandler;


    /**
     * Method to create a new user via the user management. This page is only available for users with the role "ADMIN".
     */
    public void createNewUser() {
        Userx user = new Userx();
        roles = new HashSet<>();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(true);

        selectedRoles.forEach(x -> {
            if (x.equals("ADMIN")) {
                roles.add(UserRole.ADMIN);
                roles.add(UserRole.GARDENER);
            } else if (x.equals("GARDENER")) {
                roles.add(UserRole.GARDENER);
            }
        });
        roles.add(UserRole.USER);
        user.setRoles(roles);

        if (userxRepository.findFirstByUsername(user.getUsername()) != null) {
            try {
                warningOrErrorFileHandler = new FileHandler("src/main/logs/warning_and_error_logs.log", true);
                warningOrErrorFileHandler.setFormatter(new SimpleFormatter());
                warningOrErrorLogger.addHandler(warningOrErrorFileHandler);
                warningOrErrorLogger.warning("USERNAME TAKEN: " + user.getUsername());
                warningOrErrorFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log creationFailLog = new Log();
            creationFailLog.setDate(LocalDate.now());
            creationFailLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            creationFailLog.setAuthor(sessionInfoBean.getCurrentUserName());
            creationFailLog.setSubject("USER CREATION FAILED");
            creationFailLog.setText("ENTERED USERNAME ALREADY TAKEN: " + user.getUsername());
            creationFailLog.setType(LogType.WARNING);
            logRepository.save(creationFailLog);
        } else {
            try {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
                successLogger.info(CREATE + user.getUsername());
                successFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log createLog = new Log();
            createLog.setDate(LocalDate.now());
            createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            createLog.setAuthor(sessionInfoBean.getCurrentUserName());
            createLog.setSubject("USER CREATION");
            createLog.setText(CREATE + user.getUsername());
            createLog.setType(LogType.SUCCESS);
            logRepository.save(createLog);
            userService.saveUser(user);
        }
    }

    /**
     * Method to register a new user account. Users can create new accounts on the register page only providing an username and a password.
     * They can change other user details on their settings page after logging in.
     *
     * @throws IOException
     */

    public void registerNewUser() throws IOException {
        Userx user = new Userx();
        roles = new HashSet<>();
        roles.add(UserRole.USER);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRoles(roles);

        if (userxRepository.findFirstByUsername(user.getUsername()) != null) {
            try {
                warningOrErrorFileHandler = new FileHandler("src/main/logs/warning_and_error_logs.log", true);
                warningOrErrorFileHandler.setFormatter(new SimpleFormatter());
                warningOrErrorLogger.addHandler(warningOrErrorFileHandler);
                warningOrErrorLogger.warning("USERNAME TAKEN:" + user.getUsername());
                warningOrErrorFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log creationFailLog = new Log();
            creationFailLog.setDate(LocalDate.now());
            creationFailLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            creationFailLog.setAuthor(sessionInfoBean.getCurrentUserName());
            creationFailLog.setSubject("USER CREATION FAILED");
            creationFailLog.setText("ENTERED USERNAME ALREADY TAKEN: " + user.getUsername());
            creationFailLog.setType(LogType.WARNING);
            logRepository.save(creationFailLog);

            // redirect to login page
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
        } else {
            try {
                successFileHandler = new FileHandler("src/main/logs/success_logs.log", true);
                successFileHandler.setFormatter(new SimpleFormatter());
                successLogger.addHandler(successFileHandler);
                successLogger.info(CREATE + user.getUsername());
                successFileHandler.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log createLog = new Log();
            createLog.setDate(LocalDate.now());
            createLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            createLog.setAuthor(sessionInfoBean.getCurrentUserName());
            createLog.setSubject(CREATE);
            createLog.setText(CREATE + user.getUsername());
            createLog.setType(LogType.SUCCESS);
            logRepository.save(createLog);

            userService.saveUser(user);
            // redirect to login page
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
        }
    }
}
