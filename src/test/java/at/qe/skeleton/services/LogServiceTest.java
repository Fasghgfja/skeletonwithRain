package at.qe.skeleton.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import at.qe.skeleton.model.Log;
import at.qe.skeleton.repositories.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Test
    void testGetAllLogs() {
        logRepository.save(new Log());
        logRepository.save(new Log());
        logRepository.save(new Log());

        List<Log> logs = Arrays.asList(
                new Log(),
                new Log(),
                new Log()
        );

        when(logRepository.findAll()).thenReturn(logs);

        Collection<Log> result = logService.getAllLogs();
        assertEquals(logs, result);

        verify(logRepository).findAll();
    }
}