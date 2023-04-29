package at.qe.skeleton.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.model.LogType;
import at.qe.skeleton.repositories.LogRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class LogServiceTest {

    @Autowired
    LogService logService;

    /**
     * Test for the saveLog method of the LogService.
     * New log is created and saved to database.
     */

    @DirtiesContext
    @Test
    void testSaveLog(){
        Log newLog = new Log();
        newLog.setId(1L);
        newLog.setDate(LocalDate.now());
        newLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog.setSubject("TEST LOG");
        newLog.setText("TEST LOG TEXT");
        newLog.setType(LogType.SUCCESS);
        logService.saveLog(newLog);

        assertEquals(newLog, logService.getLogById(1L));
    }

    /**
     * Testing saveLog method of LogService.
     * Multiple log are created and checked if they are stored in the database.
     */

    @Test
    @DirtiesContext
    void testSaveMultipleLog(){
        Log newLog = new Log();
        newLog.setId(1L);
        newLog.setDate(LocalDate.now());
        newLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog.setSubject("TEST LOG");
        newLog.setText("TEST LOG TEXT");
        newLog.setType(LogType.SUCCESS);
        logService.saveLog(newLog);

        Log newLog2 = new Log();
        newLog2.setId(2L);
        newLog2.setDate(LocalDate.now());
        newLog2.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog2.setSubject("TEST LOG 2");
        newLog2.setText("TEST LOG TEXT 2");
        newLog2.setType(LogType.SUCCESS);
        logService.saveLog(newLog2);


        assertEquals(newLog, logService.getLogById(1L));
        assertEquals(newLog2, logService.getLogById(2L));
    }

    /**
     * Testing the getLogsAmount method of the LogService.
     * First the initial value is stored in the variable startValue.
     * Then a new log is created with the saveLog method tested before.
     * Afterwards it is checked if the amount of logs has increased by 1.
     */

    @Test
    @DirtiesContext
    void testGetLogsAmount(){
        long startValue = logService.getLogsAmount();
        logService.saveLog(new Log());
        assertEquals(startValue + 1, logService.getLogsAmount());
    }

    /**
     * Testing the getAllLogs method of the LogService.
     * Therefore, the getAllLogs method is called and its size is calculated.
     * This result is then compared to the result given by the getLogsAmount method tested above.
     */
    @Test
    @DirtiesContext
    void testGetAllLogs(){
        assertEquals(logService.getLogsAmount(), logService.getAllLogs().size());
    }

    /**
     * Testing the getErrorLogsAmount method of the LogService.
     * The errorLogsAmountStart value is initialized before a new error log is created.
     * Also a new warning log is created.
     * Afterwards it is checked if the amount of error logs has increased by 1.
     * It is also checked if the amount of total logs has increased by 2 with the getLogsAmount method tested above.
     */

    @Test
    @DirtiesContext
    void getErrorLogsAmount(){
        long errorLogsAmountStart = logService.getErrorLogsAmount();
        long totalAmountStart = logService.getLogsAmount();

        Log newLog = new Log();
        newLog.setId(1L);
        newLog.setDate(LocalDate.now());
        newLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog.setSubject("TEST LOG");
        newLog.setText("TEST LOG TEXT");
        newLog.setType(LogType.ERROR);
        logService.saveLog(newLog);

        Log newLog2 = new Log();
        newLog2.setId(2L);
        newLog2.setDate(LocalDate.now());
        newLog2.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog2.setSubject("TEST LOG 2");
        newLog2.setText("TEST LOG TEXT 2");
        newLog2.setType(LogType.WARNING);
        logService.saveLog(newLog2);

        assertEquals(errorLogsAmountStart + 1, logService.getErrorLogsAmount());
        assertEquals(totalAmountStart + 2, logService.getLogsAmount());
    }


    /**
     * Testing the getWarningLogs method of the LogService.
     * The warningLogsAmount value is initialized before a new warning log is created.
     * Also a new error log is created.
     * Afterwards it is checked if the amount of warning logs has increased by 1.
     * It is also checked if the amount of total logs has increased by 2 with the getLogsAmount method tested above.
     */

    @Test
    @DirtiesContext
    void getWarningLogsAmount(){
        long warningLogsAmountStart = logService.getWarningLogsAmount();
        long totalAmountStart = logService.getLogsAmount();

        Log newLog = new Log();
        newLog.setId(1L);
        newLog.setDate(LocalDate.now());
        newLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog.setSubject("TEST LOG");
        newLog.setText("TEST LOG TEXT");
        newLog.setType(LogType.WARNING);
        logService.saveLog(newLog);

        Log newLog2 = new Log();
        newLog2.setId(2L);
        newLog2.setDate(LocalDate.now());
        newLog2.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog2.setSubject("TEST LOG 2");
        newLog2.setText("TEST LOG TEXT 2");
        newLog2.setType(LogType.ERROR);
        logService.saveLog(newLog2);

        assertEquals(warningLogsAmountStart + 1, logService.getWarningLogsAmount());
        assertEquals(totalAmountStart + 2, logService.getLogsAmount());
    }


    /**
     * Testing the getSuccessLogsAmount method of the LogService.
     * The successLogsAmount value is initialized before a new success log is created.
     * Also a new error log is created.
     * Afterwards it is checked if the amount of success logs has increased by 1.
     * It is also checked if the amount of total logs has increased by 2 with the getLogsAmount method tested above.
     */

    @Test
    @DirtiesContext
    void getSuccessLogsAmount(){
        long successLogsAmount = logService.getSuccessLogsAmount();
        long totalAmountStart = logService.getLogsAmount();

        Log newLog = new Log();
        newLog.setId(1L);
        newLog.setDate(LocalDate.now());
        newLog.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog.setSubject("TEST LOG");
        newLog.setText("TEST LOG TEXT");
        newLog.setType(LogType.SUCCESS);
        logService.saveLog(newLog);

        Log newLog2 = new Log();
        newLog2.setId(2L);
        newLog2.setDate(LocalDate.now());
        newLog2.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newLog2.setSubject("TEST LOG 2");
        newLog2.setText("TEST LOG TEXT 2");
        newLog2.setType(LogType.ERROR);
        logService.saveLog(newLog2);

        assertEquals(successLogsAmount + 1, logService.getSuccessLogsAmount());
        assertEquals(totalAmountStart + 2, logService.getLogsAmount());
    }


}