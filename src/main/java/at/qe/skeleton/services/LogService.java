package at.qe.skeleton.services;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import at.qe.skeleton.repositories.UserxRepository;
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

    @Autowired
    private UserxRepository userxRepository;

    /**
     * The method Returns all Logs.
     *
     * @return a Collection of all log entries.
     */

    public Collection<Log> getAllLogs() {
        return logRepository.findAll();
    }


    public long getLogsAmount() {
        return logRepository.count();
    }


    public long getErrorLogsAmount() {
        return logRepository.countLogByType(LogType.ERROR);
    }


    public long getWarningLogsAmount() {
        return logRepository.countLogByType(LogType.WARNING);
    }


    public long getSuccessLogsAmount() {
        return logRepository.countLogByType(LogType.SUCCESS);
    }


    //TODO: implement this placeholder method
    public Log createLogEntry(){
        Log logEntry = new Log();
        return logEntry;
    }

    @PreAuthorize("permitAll()")
    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    public Log getLogById(Long id){
        return logRepository.findFirstById(id);
    }

}