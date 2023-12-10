package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Page {

    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;

    public HomePage(final User user) {
        super(user);
        this.likedSongs = user.getLikedSongs();
        this.followedPlaylists = user.getFollowedPlaylists();

    }

    @Override
    public String printPage() {
        List<String> songs = new ArrayList<>();
        for (Song song : likedSongs) {
            songs.add(song.getName());
        }
        List<String> playlists = new ArrayList<>();
        for (Playlist playlist : followedPlaylists) {
            playlists.add(playlist.getName());
        }
        return
                "Liked songs:\n\t" + songs + "\n\n"
                        + "Followed playlists:\n\t" + playlists;
    }

}
