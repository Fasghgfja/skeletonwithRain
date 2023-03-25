package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    public Integer getLogsAmount() {
        return logRepository.findAll().stream().toList().size();
    }


    //TODO: implement this placeholder method
    public Log createLogEntry(){
        Log logEntry = new Log();
        return logEntry;
    }

    public void saveLog(Log log) {
        logRepository.save(log);
    }

}