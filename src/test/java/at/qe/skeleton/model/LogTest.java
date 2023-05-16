package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class LogTest {

    /**
     * Testing getter and setter for all fields of the Log model.
     */
    @Test
    void testLog() {
        Long id = 1L;
        String text = "Test log text";
        String subject = "DELETION";
        String author = "test-author";
        LocalDate date = LocalDate.now();
        LogType type = LogType.SUCCESS;
        Log log = new Log();

        // Test default values
        assertNull(log.getId());
        assertNull(log.getText());
        assertNull(log.getSubject());
        assertNull(log.getAuthor());
        assertNull(log.getDate());
        assertNull(log.getType());

        // Test setters and getters

        log.setId(id);
        log.setText(text);
        log.setSubject(subject);
        log.setAuthor(author);
        log.setDate(date);
        log.setType(type);

        assertEquals(id, log.getId());
        assertEquals(text, log.getText());
        assertEquals(subject, log.getSubject());
        assertEquals(author, log.getAuthor());
        assertEquals(date, log.getDate());
        assertEquals(type, log.getType());
    }

    /**
     * Testing compareTo() method of the Log model.
     */
    @Test
    void testCompareTo(){
        Log log1 = new Log();
        log1.setId(1L);
        Log log2 = new Log();
        log2.setId(2L);

        assertTrue(log1.compareTo(log2) < 0);
        assertTrue(log2.compareTo(log1) > 0);
    }

    @Test
    void testHashCode(){
        Log log1 = new Log();
        log1.setId(1L);
        Log log2 = new Log();
        log2.setId(2L);

        assertNotEquals(log1.hashCode(), log2.hashCode());
    }
}

