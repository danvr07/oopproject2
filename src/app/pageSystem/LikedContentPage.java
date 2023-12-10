package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage extends Page{

    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;




    public LikedContentPage(User user) {
        super(user);
        this.likedSongs = user.getLikedSongs();
        this.followedPlaylists = user.getFollowedPlaylists();
    }

    @Override
    public String printPage() {
        List<String> songs = new ArrayList<>();
        for (Song song : likedSongs) {
            String songDetails = song.getName() + " - " + song.getArtist();
            songs.add(songDetails);
        }
        List<String> playlists = new ArrayList<>();
        for (Playlist playlist : followedPlaylists) {
            String playlistDetails = playlist.getName() + " - " + playlist.getOwner();
            playlists.add(playlistDetails);
        }
        return
                "Liked songs:\n\t" + songs + "\n\n" +
                        "Followed playlists:\n\t" + playlists;
    }
}
