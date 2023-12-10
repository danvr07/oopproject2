package app.info;

public class Merch {

    String name;
    String description;
    int price;

    public int getPrice() {
        return price;
    }

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

    public void setPrice(int price) {
        this.price = price;
    }

    public Merch(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
