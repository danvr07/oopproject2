package app.pageSystem;

import app.user.User;

public class LikedContentPage extends Page{


    public LikedContentPage(User user) {
        super(user);
    }

    @Override
    public String printPage() {
        return "Pagina de conținuturi favorite pentru utilizatorul " + getUsername();
    }
}
