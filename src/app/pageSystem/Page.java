package app.pageSystem;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * The class represents a content page associated with a user.
 * It contains information about the user whose page it is and the owner of the page.
 */

@Getter
@Setter
public class Page {
    /**
     * The user associated with this page.
     */
    private User user;

    public Page(final User user) {
        this.user = user;
    }

    /**
     * Generates and returns a string representation of the page's content.
     *
     * @return A string representing the page's content.
     */
    public String printPage() {
        return "Pagina de conținuturi" + user.getUsername();
    }

}
