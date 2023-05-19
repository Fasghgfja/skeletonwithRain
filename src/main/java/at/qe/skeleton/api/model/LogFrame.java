package at.qe.skeleton.api.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LogFrame implements Serializable {


    private String text;
    private String subject;
    private String author;
    private String time_stamp;
    private String type;

    @Override
    public String toString() {
        return "LogFrame{" +
                "txt='" + text + '\'' +
                ", subject='" + subject + '\'' +
                ", author='" + author + '\'' +
                ", timeStamp='" + time_stamp + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
