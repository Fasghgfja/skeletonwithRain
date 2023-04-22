package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Scope("request")
public class DashboardBean {

    @Autowired
    private UserxRepository userxRepository;
    public String getDashboardOutcome() {
        Set<UserRole> role = getAuthenticatedUser().getRoles();
        if (role.contains(UserRole.ADMIN)) {
            return "/adminDashboard.xhtml";
        } else if (role.contains(UserRole.GARDENER)) {
            return "/gardenerDashboard.xhtml";
        } else {
            return "/userDashboard.xhtml";
        }
    }

    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userxRepository.findFirstByUsername(auth.getName());
    }
}