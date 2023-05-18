package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.services.LogService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller for the Log list view.
 */
@Getter
@Setter
@Component
@Scope("view")
public class LogController {


    private List<Log> latestLogs;

    private List<Log> logList;



    @Autowired
    private LogService logService;

    @PostConstruct
    public void init(){
        logList = (ArrayList<Log>) logService.getAllLogs();
    }

    /**
     * Method to return all logs.
     * @return Collection of all logs stored in the database.
     */

    public Collection<Log> getLogs() {
        return logService.getAllLogs();
    }

    /**
     * Get the amount of all logs in the database.
     * @return long of the amount of logs.
     */
    public long getLogsAmount() {
        return logService.getLogsAmount();
    }

    /**
     * Get the amount of all error logs.
     * @return long of the amount of error logs.
     */
    public long getErrorLogsAmount(){
        return logService.getErrorLogsAmount();
    }

    /**
     * Get the amount of all warning logs.
     * @return long of the amount of warninig logs.
     */
    public long getWarningLogsAmount(){
        return logService.getWarningLogsAmount();
    }

    /**
     * Get the amount of all success logs.
     * @return long of the amount of success logs.
     */
    public long getSuccessLogsAmount(){
        return logService.getSuccessLogsAmount();
    }


}
