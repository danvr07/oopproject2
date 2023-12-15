package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pageSystem.HomePage;
import app.pageSystem.LikedContentPage;
import app.pageSystem.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.CommandInput;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static app.Admin.verification;
import static app.Admin.users;
import static app.Admin.updateFollowedPlaylists;
import static app.Admin.decrementFollowers;


/**
 * The type User.
 */

@Getter
@Setter
public class User extends LibraryEntry {

    private String username;

    private int age;

    private String city;

    private ArrayList<Playlist> playlists;

    private ArrayList<Song> likedSongs;

    private ArrayList<Playlist> followedPlaylists;

    private final Player player;

    private final SearchBar searchBar;

    private boolean lastSearched;

    private boolean online = true;

    private String type = "regular";

    private Page currentPage;

    public static LibraryEntry albumloaded;

    /**
     * Instantiates a new Artist/Host.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     * @param online   the online
     * @param type     the type
     */

    @Builder
    public User(final String username, final int age, final String city,
                final boolean online, final String type) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        this.online = online;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        this.type = type;
    }

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    @Builder
    public User(final String username, final int age, final String city) {
        super(username);
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        currentPage = new HomePage(this);
    }

    /**
     * Search array list.
     *
     * @param filters      the filters
     * @param typeSearched the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String typeSearched) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, typeSearched);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected != null && selected.isArtist()) {
            this.currentPage = ((Artist) selected).getCurrentPage();
        } else if (selected != null && selected.isHost()) {
            this.currentPage = ((Host) selected).getCurrentPage();
        }

        if (selected == null) {
            return "The selected ID is too high.";
        }
        if (selected.isArtist() || selected.isHost()) {
            return "Successfully selected %s's page.".formatted(selected.getName());
        } else {
            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }
        if (searchBar.getLastSearchType().equals("album")) {
            albumloaded = searchBar.getLastSelected();
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();


        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }


        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Changes the current page of the user based on the specified command.
     *
     * @param page The name of the page that the user wants to access.
     * @return A message indicating whether the page change was successful or not.
     */
    public String changePage(final String page) {
        if (page.equals("Home")) {
            this.currentPage = new HomePage(this);
            return username + " accessed " + page + " successfully.";
        } else if (page.equals("LikedContent")) {
            if (this.type.equals("regular")) {
                this.currentPage = new LikedContentPage(this);
                return username + " accessed " + page + " successfully.";
            } else {
                return username + " is trying to access a non-existent page.";
            }
        } else {
            return "Invalid page.";
        }
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song")
                && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String typeSearch = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!typeSearch.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (isOnline()) {
            player.simulatePlayer(time);
        }
    }

    /**
     * Switch connection status.
     */
    public void switchConnectionStatus() {
        this.online = !this.online;
    }

    /**
     * Update liked songs.
     *
     * @param songName the song name
     */
    public void updateLikedSongs(final String songName) {
        Iterator<Song> iterator = likedSongs.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getName().equals(songName)) {
                iterator.remove();
            }
        }
    }

    /**
     * Update followed playlists.
     *
     * @param playlistName the playlist name
     */
    public void updateFollowedPlaylistsUser(final String playlistName) {
        Iterator<Playlist> iterator = followedPlaylists.iterator();
        while (iterator.hasNext()) {
            Playlist playlist = iterator.next();
            if (playlist.getName().equals(playlistName)) {
                iterator.remove();
            }
        }
    }


    /**
     * Removes an event associated with the specified event name.
     *
     * @param eventName The name of the event to be removed.
     * @return A message indicating that the user is not an artist.
     */
    public String removeEvent(final String eventName) {
        return " is not an artist.";
    }

    /**
     * Adds an album based on the provided command input.
     *
     * @param commandInput The input containing information about the album.
     * @return A message indicating that the user is not an artist.
     */
    public String addAlbum(final CommandInput commandInput) {
        return " is not an artist.";
    }

    /**
     * Removes an announcement associated with the specified announcement name.
     *
     * @param announcementName The name of the announcement to be removed.
     * @return A message indicating that the user is not a host.
     */
    public String removeAnnouncement(final String announcementName) {
        return getUsername() + " is not a host.";
    }

    /**
     * Adds an event based on the provided command input.
     *
     * @param commandInput The input containing information about the event.
     * @return A message indicating that the user is not an artist.
     */
    public String addEvent(final CommandInput commandInput) {
        return getUsername() + " is not an artist.";
    }

    /**
     * Adds an announcement based on the provided command input.
     *
     * @param commandInput The input containing information about the announcement.
     * @return A message indicating that the user is not a host.
     */
    public String addAnnouncement(final CommandInput commandInput) {
        return getUsername() + " is not a host.";
    }

    /**
     * Removes a podcast associated with the specified podcast name.
     *
     * @param podcastName The name of the podcast to be removed.
     * @return A message indicating that the user is not a host.
     */
    public String removePodcast(final String podcastName) {
        return getUsername() + " is not a host.";
    }

    /**
     * Adds a podcast based on the provided command input.
     *
     * @param commandInput The input containing information about the podcast.
     * @return A message indicating that the user is not a host.
     */
    public String addPodcast(final CommandInput commandInput) {
        return " is not a host.";
    }

    /**
     * Adds merchandise based on the provided command input.
     *
     * @param commandInput The input containing information about the merchandise.
     * @return A message indicating that the user is not an artist.
     */
    public String addMerch(final CommandInput commandInput) {
        return getUsername() + " is not an artist.";
    }

    /**
     * Removes an album associated with the specified album name.
     *
     * @param albumName The name of the album to be removed.
     * @return A message indicating that the user is not an artist.
     */
    public String removeAlbum(final String albumName) {
        return getUsername() + " is not an artist.";
    }

    /**
     * Deletes a regular user from the application based on the provided username.
     *
     * @param username The username of the user to be deleted.
     * @return A message indicating the success or failure of the operation.
     */
    public String deleteThis(final String username, final String pageVisited) {
        if (verification(username).equals("playlist")) {
            return username + " can't be deleted.";
        }
        User user = Admin.getUser(username);

        if (user != null) {
            updateFollowedPlaylists(user);
            decrementFollowers(user);
            users.remove(user);
            return username + " was successfully deleted.";
        }

        return "The username " + username + " doesn't exist.";
    }

}


