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

    /**
     * Method to get all logs currently saved in the database.
     * @return number of all logs.
     */

    public long getLogsAmount() {
        return logRepository.count();
    }


    /**
     * Method to get all logs with type 'ERROR' currently saved in the database.
     * @return number of all logs of type 'ERROR'.
     */
    public long getErrorLogsAmount() {
        return logRepository.countLogByType(LogType.ERROR);
    }


    /**
     * Method to get all logs with type 'WARNING' currently saved in the database.
     * @return number of all logs of type 'WARNING'.
     */
    public long getWarningLogsAmount() {
        return logRepository.countLogByType(LogType.WARNING);
    }

    /**
     * Method to get all logs with type 'SUCCESS' currently saved in the database.
     * @return number of all logs of type 'SUCCESS'.
     */

    public long getSuccessLogsAmount() {
        return logRepository.countLogByType(LogType.SUCCESS);
    }


    //TODO: implement this placeholder method
    public Log createLogEntry(){
        Log logEntry = new Log();
        return logEntry;
    }

    /**
     * Method to save a log to the database.
     * @param log The log to be saved.
     * @return the saved log.
     */
    @PreAuthorize("permitAll()")
    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    /**
     * Method to get the log with the given id from the database.
     * @param id identifier of the log.
     * @return Log with given id.
     */
    public Log getLogById(Long id){
        return logRepository.findFirstById(id);
    }

}