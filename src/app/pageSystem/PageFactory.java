package app.pageSystem;

import app.user.User;

/**
 * The class represents a factory for creating pages.
 */
public class PageFactory {

    /**
     * Creates a page for the given user.
     *
     * @param user the user
     * @return the page
     */

    public static Page createPage(final User user) {
        switch (user.getType()) {
            case "host":
                return new HostPage(user);
            case "artist":
                return new ArtistPage(user);
            default:
                return new HomePage(user);
        }
    }
}
