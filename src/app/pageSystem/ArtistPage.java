package app.pageSystem;

import app.Admin;
import app.audio.Collections.Album;
import app.info.Event;
import app.info.Merch;
import app.user.Artist;
import app.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ArtistPage implements Page {
    private String username;
    private List<Album> albums;
    private List<Merch> merchs;
    private List<Event> events;


    public ArtistPage(final User user) {
        username = user.getUsername();
        albums = ((Artist) user).getAlbums();
        merchs = ((Artist) user).getMerchs();
        events = ((Artist) user).getEvents();

    }

    @Override
    public String printPage() {
        Artist artist = (Artist) Admin.getUser(username);

        List<String> albumsNames = new ArrayList<>();
        for (Album album : albums) {
            albumsNames.add(album.getName());
        }
        List<String> merchsNames = new ArrayList<>();
        for (Merch merch : merchs) {
            String merchString = String.format("%s - %s:\n\t%s", merch
                    .getName(), merch.getPrice(), merch.getDescription());
            merchsNames.add(merchString);
        }

        List<String> eventsNames = new ArrayList<>();
        for (Event event : events) {
            String eventString = String.format("%s - %s:\n\t%s", event.getName(), artist
                    .formatDate(event.getDate()), event.getDescription());
            eventsNames.add(eventString);
        }

        return
                "Albums:\n\t" + albumsNames + "\n\n"
                        + "Merch:\n\t" + merchsNames + "\n\n"
                        + "Events:\n\t" + eventsNames;
    }

}
