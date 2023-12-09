package app.pageSystem;

import app.user.User;

public class Page {
    private User user;

    public Page(User user) {
        this.user = user;
    }

    public User getUsername() {
        return user;
    }

    public String printPage() {
        return "Pagina de conținuturi favorite pentru utilizatorul " + user.getUsername();
    }

}
