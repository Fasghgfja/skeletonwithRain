package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogFrameTest {

    @Test
    void toString_ValidLogFrame_ReturnsStringRepresentation() {
        LogFrame logFrame = new LogFrame();
        logFrame.setText("Sample text");
        logFrame.setSubject("Sample subject");
        logFrame.setAuthor("Sample author");
        logFrame.setTime_stamp("2023-06-14 12:34:56");
        logFrame.setType("Sample type");

        String expectedString = "LogFrame{" +
                "txt='Sample text" + '\'' +
                ", subject='Sample subject" + '\'' +
                ", author='Sample author" + '\'' +
                ", timeStamp='2023-06-14 12:34:56" + '\'' +
                ", type='Sample type" + '\'' +
                '}';

        String result = logFrame.toString();

        assertEquals(expectedString, result);
    }
}
