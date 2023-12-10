package app.pageSystem;

import app.user.User;

public class Page {
    private User user;

    public Page(final User user) {
        this.user = user;
    }

    public User getUsername() {
        return user;
    }

    public String printPage() {
        return "Pagina de conținuturi" + user.getUsername();
    }

}
