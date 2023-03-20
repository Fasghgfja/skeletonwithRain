package at.qe.skeleton.spring;

import at.qe.skeleton.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


/**
 * This handler is triggered after a login is performed.
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 *
 */
@Component
public class LoginSuccessHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private LogService logService;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        //Audit Log
        logService.createLogEntry();
    }

}
