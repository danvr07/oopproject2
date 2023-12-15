package app.pageSystem;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public final class HomePage implements Page {

    private static final int MAX = 5;

    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;

    public HomePage(final User user) {
        this.likedSongs = user.getLikedSongs();
        this.followedPlaylists = user.getFollowedPlaylists();

    }

    private List<Song> getSortedLikedSongs() {
        List<Song> sortedLikedSongs = new ArrayList<>(this.likedSongs);
        Collections.sort(sortedLikedSongs, Comparator.comparingInt(Song::getLikes).reversed());
        return sortedLikedSongs;
    }

    private List<Playlist> getSortedFollowedPlaylists() {
        List<Playlist> sortedFollowedPlaylists = new ArrayList<>(this.followedPlaylists);
        Collections.sort(sortedFollowedPlaylists, Comparator
                .comparingInt(Playlist::getFollowers).reversed());
        return sortedFollowedPlaylists;
    }

    private List<Song> getTop5LikedSongs() {
        List<Song> sortedLikedSongs = getSortedLikedSongs();
        return sortedLikedSongs.subList(0, Math.min(sortedLikedSongs.size(), MAX));
    }

    private List<Playlist> getTop5FollowedPlaylists() {
        List<Playlist> sortedFollowedPlaylists = getSortedFollowedPlaylists();
        return sortedFollowedPlaylists.subList(0, Math.min(sortedFollowedPlaylists.size(), MAX));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String printPage() {

        List<String> songs = new ArrayList<>();
        for (Song song : getTop5LikedSongs()) {
            songs.add(song.getName());
        }
        List<String> playlists = new ArrayList<>();
        for (Playlist playlist : getTop5FollowedPlaylists()) {
            playlists.add(playlist.getName());
        }
        return
                "Liked songs:\n\t" + songs + "\n\n"
                        + "Followed playlists:\n\t" + playlists;
    }

}
