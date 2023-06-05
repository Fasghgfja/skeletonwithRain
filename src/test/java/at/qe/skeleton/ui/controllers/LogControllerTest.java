package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Log;
import at.qe.skeleton.services.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Import(LogController.class)
class LogControllerTest {

    private LogController logController;

    @Mock
    private LogService logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logController = new LogController();
        logController.setLogService(logService);
    }

    @Test
    void testInit() {
        List<Log> expectedLogs = new ArrayList<>();
        when(logService.getAllLogs()).thenReturn(expectedLogs);

        logController.init();

        assertEquals(expectedLogs, logController.getLatestLogs());
        verify(logService, times(1)).getAllLogs();
    }

    @Test
    void testGetLogs() {
        Collection<Log> expectedLogs = new ArrayList<>();
        when(logService.getAllLogs()).thenReturn(expectedLogs);

        Collection<Log> logs = logController.getLogs();

        assertEquals(expectedLogs, logs);
        verify(logService, times(1)).getAllLogs();
    }

    @Test
    void testGetLogsAmount() {
        when(logService.getLogsAmount()).thenReturn(10L);

        long amount = logController.getLogsAmount();

        assertEquals(10L, amount);
        verify(logService, times(1)).getLogsAmount();
    }

    @Test
    void testGetErrorLogsAmount() {
        when(logService.getErrorLogsAmount()).thenReturn(5L);

        long amount = logController.getErrorLogsAmount();

        assertEquals(5L, amount);
        verify(logService, times(1)).getErrorLogsAmount();
    }

    @Test
    void testGetWarningLogsAmount() {
        when(logService.getWarningLogsAmount()).thenReturn(3L);

        long amount = logController.getWarningLogsAmount();

        assertEquals(3L, amount);
        verify(logService, times(1)).getWarningLogsAmount();
    }

    @Test
    void testGetSuccessLogsAmount() {
        when(logService.getSuccessLogsAmount()).thenReturn(2L);

        long amount = logController.getSuccessLogsAmount();

        assertEquals(2L, amount);
        verify(logService, times(1)).getSuccessLogsAmount();
    }
}
