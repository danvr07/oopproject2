package app.pageSystem;

import app.user.User;

public class HostPage extends Page{


    public HostPage(User user) {
        super(user);
    }

    @Override
    public String printPage() {
        return "Pagina de conținuturi favorite pentru utilizatorul " + getUsername();
    }
}
