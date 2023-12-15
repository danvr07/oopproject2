package app.info;

import java.text.ParseException;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private String name;
    private String description;
    private Date date;

    public Event(final String name, final String description,
                 final Date dateString) throws ParseException {
        this.name = name;
        this.description = description;
        this.date = dateString;
    }

}
