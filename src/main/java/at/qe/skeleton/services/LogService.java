package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service for accessing and manipulating the logs.
 **/
@Service
@Scope("application")
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     * The method Returns all Logs.
     *
     * @return a Collection of all log entries.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Log> getAllLogs() {
        return logRepository.findAll();
    }
}