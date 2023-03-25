package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.services.LogService;
import org.primefaces.model.ResponsiveOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller for the Log list view.
 */
@Component
@Scope("view")
public class LogListController {


    private List<Log> latestLogs;



    @Autowired
    private LogService logService;

    public Collection<Log> getLogs() {
        return logService.getAllLogs();
    }



    public Collection<Log> getLatestlogs() {
        latestLogs = new ArrayList<>();
        latestLogs.addAll(logService.getAllLogs());
        return latestLogs;
    }


}
