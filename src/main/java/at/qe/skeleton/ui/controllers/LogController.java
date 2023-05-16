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

    public Collection<Log> getLogs() {
        return logService.getAllLogs();
    }


    public long getLogsAmount() {
        return logService.getLogsAmount();
    }

    public long getErrorLogsAmount(){
        return logService.getErrorLogsAmount();
    }
    public long getWarningLogsAmount(){
        return logService.getWarningLogsAmount();
    }
    public long getSuccessLogsAmount(){
        return logService.getSuccessLogsAmount();
    }


}
