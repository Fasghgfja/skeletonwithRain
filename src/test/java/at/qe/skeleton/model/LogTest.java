package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class LogTest {

    @Test
    void testLog() {
        Long id = 1L;
        String text = "Test log text";
        String subject = "DELETION";
        String author = "test-author";
        LocalDate date = LocalDate.now();
        Log log = new Log();

        // Test default values
        assertNull(log.getId());
        assertNull(log.getText());
        assertNull(log.getSubject());
        assertNull(log.getAuthor());
        assertNull(log.getDate());

        // Test setters and getters

        log.setId(id);
        log.setText(text);
        log.setSubject(subject);
        log.setAuthor(author);
        log.setDate(date);

        assertEquals(id, log.getId());
        assertEquals(text, log.getText());
        assertEquals(subject, log.getSubject());
        assertEquals(author, log.getAuthor());
        assertEquals(date, log.getDate());
    }
}

