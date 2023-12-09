package app.info;

public class Announcement {
    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Announcement(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
