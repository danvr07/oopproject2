package app.info;

import java.text.ParseException;
import java.util.Date;

public class Event {
    String name;
    String description;
    private Date date;

    public String getName() {
        return name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public Event(String name, String description, Date dateString) throws ParseException {
        this.name = name;
        this.description = description;
        this.date = dateString;
    }

}
